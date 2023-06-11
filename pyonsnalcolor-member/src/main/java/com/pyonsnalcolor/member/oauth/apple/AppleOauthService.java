package com.pyonsnalcolor.member.oauth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.oauth.apple.dto.ApplePublicKeyDto;
import com.pyonsnalcolor.member.oauth.apple.dto.ApplePublicKeysDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
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
    private static final String ALG = "alg";
    private static final String KID = "kid";

    @Value("${spring.security.oauth2.apple.key-uri}")
    private String keyUri;

    @Value("${spring.security.oauth2.apple.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.apple.issuer}")
    private String issuer;

    public String getEmail(LoginRequestDto loginRequestDto) {
        String identityToken = loginRequestDto.getToken();
        PublicKey publicKeyForParseIdentityToken = createPublicKeyForParseIdentityToken(identityToken);

        Claims claims = parseAndValidateClaims(identityToken, publicKeyForParseIdentityToken);
        return claims.get(EMAIL, String.class);
    }

    private PublicKey createPublicKeyForParseIdentityToken(String identityToken) {
        ApplePublicKeyDto selectedApplePublicKey = selectApplePublicKeyMatchesIdentityToken(identityToken);

        byte[] nBytes = Base64.getUrlDecoder().decode(selectedApplePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(selectedApplePublicKey.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(selectedApplePublicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
            throw new JwtException("Identity token을 파싱할 공개키를 생성할 수 없습니다.");
        }
    }

    private ApplePublicKeyDto selectApplePublicKeyMatchesIdentityToken(String identityToken) {
        Map<String, String> headerMap = parseHeaderOfIdentityToken(identityToken);
        String alg = headerMap.get(ALG);
        String kid = headerMap.get(KID);

        ApplePublicKeysDto keys = getApplePublicKeysFromApple();
        return keys.getApplePublicKeyMatchesAlgAndKid(alg, kid);
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
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(keyUri, ApplePublicKeysDto.class);
    }

    private Claims parseAndValidateClaims(String identityToken, PublicKey publicKey) {
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