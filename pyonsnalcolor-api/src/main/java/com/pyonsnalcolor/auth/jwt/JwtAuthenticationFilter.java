package com.pyonsnalcolor.auth.jwt;

import com.pyonsnalcolor.auth.CustomUserDetails;
import com.pyonsnalcolor.auth.CustomUserDetailsService;
import com.pyonsnalcolor.auth.RedisUtil;
import com.pyonsnalcolor.exception.ApiException;
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

import static com.pyonsnalcolor.exception.model.AuthErrorCode.MEMBER_LOGOUT;

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

        try {
            String accessToken = resolveBearerTokenFromHeader(request, accessTokenHeader);

            if (accessToken != null && redisUtil.isTokenBlackList(accessToken)) {
                throw new ApiException(MEMBER_LOGOUT);
            }
            if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
                saveAuthentication(accessToken);
            }
        } catch (Exception e) {
            request.setAttribute("exception", e);	// 예외를 request에 set
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