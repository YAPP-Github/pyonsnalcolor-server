package com.pyonsnalcolor.member.auth.jwt;

import com.pyonsnalcolor.member.auth.CustomUserDetails;
import com.pyonsnalcolor.member.auth.CustomUserDetailsService;
import com.pyonsnalcolor.member.auth.RedisUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    @Value("${jwt.access-token.header}")
    private String accessTokenHeader;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    private String OAUTH_ID = "oAuthId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveBearerTokenFromHeader(request, accessTokenHeader);

        if (accessToken != null && redisUtil.isTokenBlackList(accessToken)) {
            throw new JwtException("로그아웃된 사용자입니다."); // 이후에 응답 변경
        }
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            saveAuthentication(accessToken);
        }
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(String accessToken) {
        Authentication authentication = createAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication createAuthentication(String token) {
        String oAuthId = (String) jwtTokenProvider.getClaims(token).get(OAUTH_ID);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(oAuthId);
        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities());
    }

    private String resolveBearerTokenFromHeader(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        return jwtTokenProvider.resolveBearerToken(bearerToken);
    }
}