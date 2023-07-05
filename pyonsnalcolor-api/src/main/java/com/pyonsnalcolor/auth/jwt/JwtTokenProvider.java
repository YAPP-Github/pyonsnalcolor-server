package com.pyonsnalcolor.auth.jwt;

import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.*;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    private long accessTokenValidity;
    private long refreshTokenValidity;
    private SecretKey secretKey;
    private String OAUTH_ID = "oAuthId";

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecretKey,
                            @Value("${jwt.access-token.validity}") long accessTokenValidity,
                            @Value("${jwt.refresh-token.validity}") long refreshTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public TokenDto createAccessAndRefreshTokenDto(String oauthId) {
        String accessToken = createBearerTokenWithValidity(oauthId, accessTokenValidity);
        String refreshToken = createBearerTokenWithValidity(oauthId, refreshTokenValidity);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createBearerTokenWithValidity(String oauthId, long tokenValidity){
        String accessToken = createTokenWithValidity(oauthId, tokenValidity);
        return createBearerHeader(accessToken);
    }

    private String createTokenWithValidity(String oAuthId, long tokenValidity){
        Date now = new Date();
        Date expirationAt = new Date(now.getTime() + tokenValidity);

        return Jwts.builder()
                .claim(OAUTH_ID, oAuthId)
                .setIssuer(jwtIssuer)
                .setIssuedAt(now)
                .setExpiration(expirationAt)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateAccessToken(String token) throws AuthException {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthException(ACCESS_TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            throw new AuthException(ACCESS_TOKEN_MALFORMED);
        } catch (Exception e) {
            throw new AuthException(ACCESS_TOKEN_INVALID);
        }
    }

    public Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .requireIssuer(jwtIssuer)
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createBearerHeader (String token) {
        return bearerHeader + token;
    }

    public String resolveBearerToken(String token) {
        if (token != null && token.startsWith(bearerHeader)) {
            return token.substring(bearerHeader.length());
        }
        throw new AuthException(ACCESS_TOKEN_NOT_BEARER);
    }

    public Long getExpirationTime(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}