package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.oauth.apple.AppleOauthService;
import com.pyonsnalcolor.member.oauth.kakao.KakaoOauthService;
import com.pyonsnalcolor.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final AppleOauthService appleOauthService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> authorizeWithKakao(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = kakaoOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.KAKAO, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/apple")
    public ResponseEntity<TokenDto> authorizeWithApple(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = appleOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.APPLE, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }
}