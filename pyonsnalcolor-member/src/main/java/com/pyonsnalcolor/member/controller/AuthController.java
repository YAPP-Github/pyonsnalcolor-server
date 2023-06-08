package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.dto.AuthorizationRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.entity.enumtype.LoginType;
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
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> authorizationWithKakao(
            @RequestBody AuthorizationRequestDto authorizationRequestDto
    ) {
        String email = kakaoOauthService.getKakaoEmail(authorizationRequestDto);
        TokenDto tokenDto = memberService.join(LoginType.KAKAO, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }
}