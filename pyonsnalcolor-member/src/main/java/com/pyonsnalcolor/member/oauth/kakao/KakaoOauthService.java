package com.pyonsnalcolor.member.oauth.kakao;

import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.oauth.kakao.dto.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@PropertySource("classpath:application-oauth.yml")
public class KakaoOauthService {

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    @Value("${jwt.access-token.header}")
    private String accessTokenHeader;

    @Value("${spring.security.oauth2.kakao.request-uri}")
    private String requestUri;

    @Autowired
    private RestTemplate restTemplate;

    public String getEmail(LoginRequestDto loginRequestDto) {
        String accessToken = loginRequestDto.getToken();
        return getKakaoUserInfo(accessToken).getEmail();
    }

    private KakaoUserInfoDto getKakaoUserInfo (String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(accessTokenHeader, bearerHeader + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.postForObject(requestUri, request, KakaoUserInfoDto.class);
    }
}