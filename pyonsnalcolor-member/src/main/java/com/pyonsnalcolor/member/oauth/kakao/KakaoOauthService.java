package com.pyonsnalcolor.member.oauth.kakao;

import com.pyonsnalcolor.member.dto.AuthorizationRequestDto;
import com.pyonsnalcolor.member.oauth.kakao.dto.KakaoOauthTokenDto;
import com.pyonsnalcolor.member.oauth.kakao.dto.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@PropertySource("classpath:application-oauth.yml")
public class KakaoOauthService {

    @Value("${spring.security.oauth2.kakao.grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.kakao.access-token-uri}")
    private String accessTokenUri;

    @Value("${spring.security.oauth2.kakao.request-uri}")
    private String requestUri;

    public String getKakaoEmail(AuthorizationRequestDto authorizationRequestDto) {
        String accessToken = getAccessToken(authorizationRequestDto);
        return getKakaoUserInfo(accessToken).getEmail();
    }

    private String getAccessToken (AuthorizationRequestDto authorizationRequestDto) {
        KakaoOauthTokenDto kakaoOauthTokenDto = getKakaoOauthToken(authorizationRequestDto);
        return kakaoOauthTokenDto.getAccessToken();
    }

    private KakaoOauthTokenDto getKakaoOauthToken (AuthorizationRequestDto authorizationRequestDto) {
        String authorizeCode = authorizationRequestDto.getAuthorizationCode();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizeCode);

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(accessTokenUri, request, KakaoOauthTokenDto.class);
    }

    private KakaoUserInfoDto getKakaoUserInfo (String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.postForObject(requestUri, request, KakaoUserInfoDto.class);
    }
}