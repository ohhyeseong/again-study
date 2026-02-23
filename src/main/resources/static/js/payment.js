const clientKey = "test_ck_공급받은키";
const customerKey = "유저식별값"; // 실제로는 서버에서 받아오거나 토큰에서 추출
const tossPayments = TossPayments(clientKey);
const paymentWidget = tossPayments.widgets({ customerKey });

// 페이지 로드 시 실행
async function init() {
    // 1. 위젯 렌더링 (금액은 일단 예시)
    await paymentWidget.renderPaymentMethods({
        selector: "#payment-method",
        amount: { value: 15000, currency: "KRW" },
    });

    await paymentWidget.renderAgreement({ selector: "#agreement" });
}

// 결제 버튼 클릭 시
button.addEventListener("click", async function () {
    // 1. [추가] 우리 서버에 결제 준비 요청
    // productId는 현재 페이지의 상품 번호를 가져오도록 설정
    const response = await fetch(`/api/v1/payments/prepare?productId=${selectedProductId}`, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'), // JWT 토큰
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        alert("결제 준비 중 오류가 발생했습니다.");
        return;
    }

    const data = await response.json(); // 서버에서 준 orderId, amount, name 등

    // 2. [수정] 서버에서 받은 데이터로 결제 요청
    await widgets.requestPayment({
        orderId: data.orderId,             // 서버가 생성한 UUID
        orderName: data.productName,       // 서버의 상품명
        successUrl: window.location.origin + "/success.html",
        failUrl: window.location.origin + "/fail.html",
        customerEmail: data.userEmail,     // 유저 이메일
        customerName: data.userName,       // 유저 닉네임
    });
});

init();