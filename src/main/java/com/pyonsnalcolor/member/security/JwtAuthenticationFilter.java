package com.pyonsnalcolor.member.security;

import com.pyonsnalcolor.member.RedisUtil;
import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
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
    private AuthUserDetailsService authUserDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    private String OAUTH_ID = "oAuthId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = resolveBearerTokenFromHeader(request);
            validateBlackList(accessToken);
            saveAuthenticationIfValidate(accessToken);

        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(String accessToken) {
        Authentication authentication = createAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication createAuthentication(String token) {
        String oAuthId = (String) jwtTokenProvider.getClaims(token).get(OAUTH_ID);
        AuthUserDetails authUserDetails = authUserDetailsService.loadUserByUsername(oAuthId);
        return new UsernamePasswordAuthenticationToken(
                authUserDetails,
                "",
                authUserDetails.getAuthorities());
    }

    private String resolveBearerTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(accessTokenHeader);
        return jwtTokenProvider.resolveBearerToken(bearerToken);
    }

    private void validateBlackList(String accessToken) {
        if (redisUtil.isTokenBlackList(accessToken)) {
            throw new PyonsnalcolorAuthException(MEMBER_LOGOUT);
        }
    }

    private void saveAuthenticationIfValidate(String accessToken) {
        if (jwtTokenProvider.validateAccessToken(accessToken)) {
            saveAuthentication(accessToken);
        }
    }
}