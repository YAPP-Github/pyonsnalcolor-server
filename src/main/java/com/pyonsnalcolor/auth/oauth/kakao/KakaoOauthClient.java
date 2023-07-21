package com.pyonsnalcolor.auth.oauth.kakao;

import com.pyonsnalcolor.auth.dto.LoginRequestDto;
import com.pyonsnalcolor.auth.enumtype.OAuthType;
import com.pyonsnalcolor.auth.oauth.OAuthClient;
import com.pyonsnalcolor.auth.oauth.kakao.dto.KakaoUserInfoDto;
import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.exception.model.AuthErrorCode;
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
public class KakaoOauthClient implements OAuthClient {

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    @Value("${jwt.access-token.header}")
    private String accessTokenHeader;

    @Value("${spring.security.oauth2.kakao.request-uri}")
    private String requestUri;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OAuthType oAuthType() {
        return OAuthType.KAKAO;
    }

    @Override
    public String getEmail(LoginRequestDto loginRequestDto) {
        String accessToken = loginRequestDto.getToken();
        String email =  getKakaoUserInfo(accessToken).getEmail();
        if (email == null) {
            throw new PyonsnalcolorAuthException(AuthErrorCode.EMAIL_UNAUTHORIZED);
        }
        return email;
    }

    private KakaoUserInfoDto getKakaoUserInfo (String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(accessTokenHeader, bearerHeader + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.postForObject(requestUri, request, KakaoUserInfoDto.class);
    }
}