package com.pyonsnalcolor.member.auth.jwt;

import com.pyonsnalcolor.member.auth.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

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
        String accessToken = createTokenWithValidity(oauthId, accessTokenValidity);
        String refreshToken = createTokenWithValidity(oauthId, refreshTokenValidity);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createTokenWithValidity(String oauthId, long tokenValidity){
        String accessToken = createJwtTokenWithValidity(oauthId, tokenValidity);
        return createBearerHeader(accessToken);
    }

    private String createJwtTokenWithValidity(String oAuthId, long tokenValidity){
        Date now = new Date();
        Date expirationAt = new Date(now.getTime() + tokenValidity);

        Claims claims = Jwts.claims()
                .setIssuer(jwtIssuer)
                .setIssuedAt(now)
                .setExpiration(expirationAt);
        claims.put(OAUTH_ID, oAuthId);

        return Jwts.builder()
                .setIssuer(jwtIssuer)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationAt)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("토큰 형식이 잘못되어 있습니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원하지 않는 형식의 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Claim이 유효하지 않은 토큰입니다.");
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
        return null;
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