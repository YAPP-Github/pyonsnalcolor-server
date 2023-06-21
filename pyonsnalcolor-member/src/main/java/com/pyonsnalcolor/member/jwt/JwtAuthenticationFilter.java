package com.pyonsnalcolor.member.jwt;

import com.pyonsnalcolor.member.entity.CustomUserDetails;
import com.pyonsnalcolor.member.service.CustomUserDetailsService;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveBearerToken(request, accessTokenHeader);

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
        String oauthId = (String) jwtTokenProvider.getClaims(token).get("oauthId");
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(oauthId);
        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }

    private String resolveBearerToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith(bearerHeader)) {
            return bearerToken.substring(bearerHeader.length());
        }
        return null;
    }
}