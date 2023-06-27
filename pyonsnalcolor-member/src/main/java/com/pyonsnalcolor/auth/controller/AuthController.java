package com.pyonsnalcolor.auth.controller;

import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.auth.dto.LoginRequestDto;
import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.auth.oauth.apple.AppleOauthService;
import com.pyonsnalcolor.auth.oauth.kakao.KakaoOauthService;
import com.pyonsnalcolor.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final AppleOauthService appleOauthService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> loginWithKakao(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = kakaoOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.KAKAO, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/apple")
    public ResponseEntity<TokenDto> loginWithApple(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = appleOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.APPLE, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueAccessToken(
            @RequestBody TokenDto tokenDto
    ) {
        TokenDto newTokenDto = memberService.reissueAccessToken(tokenDto);
        return new ResponseEntity(newTokenDto, HttpStatus.OK);
    }
}