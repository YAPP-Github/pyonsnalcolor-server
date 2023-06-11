package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.dto.LoginRequestDto;
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
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = kakaoOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.join(LoginType.KAKAO, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueAccessToken(@RequestBody TokenDto tokenDto) {
        TokenDto newTokenDto = memberService.reissueAccessToken(tokenDto);
        return new ResponseEntity(newTokenDto, HttpStatus.OK);
    }
}