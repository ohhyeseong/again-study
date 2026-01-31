package com.example.demo.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. 요청 헤더에서 토큰 꺼내기
        String token = resolveToken(request);

        // 2. 토큰이 있고, 유효하다면?
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 3. 토큰에서 유저 이름 꺼내기
            String username = jwtTokenProvider.getUsername(token);

            // 4. 유저 정보(UserDetails) 가져오기 (DB 조회)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5, 인증 객체(Authentication) 만들기
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            // 6. "이 사람 인증됐어!"라고 SecurityContext에 저장 (도장 쾅!)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. 다음 필터로 넘어가라 (통과!)
        filterChain.doFilter(request, response);
    }

    // 헤더에서 "Bearer {토큰}" 형식으로 된 토큰을 순수 토큰만 추출하는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 글자 자르고 뒤에 토큰만 리턴
        }
        return null;
    }
}
