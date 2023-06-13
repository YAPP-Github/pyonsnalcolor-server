package com.pyonsnalcolor.member.oauth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.oauth.apple.dto.ApplePublicKeyDto;
import com.pyonsnalcolor.member.oauth.apple.dto.ApplePublicKeysDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@PropertySource("classpath:application-oauth.yml")
public class AppleOauthService {

    private static final String EMAIL = "email";
    private static final String ALGORITHM = "alg";
    private static final String KEY_ID = "kid";

    @Value("${spring.security.oauth2.apple.key-uri}")
    private String keyUri;

    @Value("${spring.security.oauth2.apple.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.apple.issuer}")
    private String issuer;

    @Autowired
    private RestTemplate restTemplate;

    public String getEmail(LoginRequestDto loginRequestDto) {
        String identityToken = loginRequestDto.getToken();
        PublicKey publicKeyForParseIdentityToken = createPublicKeyForParseIdentityToken(identityToken);

        Claims claims = parseClaims(identityToken, publicKeyForParseIdentityToken);
        return claims.get(EMAIL, String.class);
    }

    private PublicKey createPublicKeyForParseIdentityToken(String identityToken) {
        ApplePublicKeyDto applePublicKey = getApplePublicKeyMatchesIdentityToken(identityToken);

        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
            throw new JwtException("Identity token을 파싱할 공개키를 생성할 수 없습니다.");
        }
    }

    private ApplePublicKeyDto getApplePublicKeyMatchesIdentityToken(String identityToken) {
        Map<String, String> headerMap = parseHeaderOfIdentityToken(identityToken);
        String algorithm = headerMap.get(ALGORITHM);
        String keyId = headerMap.get(KEY_ID);

        ApplePublicKeysDto keys = getApplePublicKeysFromApple();
        return keys.getApplePublicKeyMatchesAlgAndKid(algorithm, keyId);
    }

    private Map<String, String> parseHeaderOfIdentityToken(String identityToken) {
        try {
            String header = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> parsedHeader = new ObjectMapper().readValue(
                    new String(Base64.getDecoder().decode(header), "UTF-8"), Map.class);
            return parsedHeader;
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new JwtException("Identity token의 형식이 유효하지 않습니다.");
        }
    }

    private ApplePublicKeysDto getApplePublicKeysFromApple() {
        return restTemplate.getForObject(keyUri, ApplePublicKeysDto.class);
    }

    private Claims parseClaims(String identityToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .requireAudience(clientId)
                    .requireIssuer(issuer)
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Identity token의 형식이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("Identity token이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Identity token이 지원하지 않는 형식입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Identity token의 Claim이 유효하지 않습니다.");
        }
    }
}