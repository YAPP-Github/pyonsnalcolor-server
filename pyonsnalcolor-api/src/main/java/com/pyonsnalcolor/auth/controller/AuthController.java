package com.pyonsnalcolor.auth.controller;

import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.auth.dto.LoginRequestDto;
import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.auth.oauth.apple.AppleOauthService;
import com.pyonsnalcolor.auth.oauth.kakao.KakaoOauthService;
import com.pyonsnalcolor.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthController", description = "사용자 인증/인가용 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final AppleOauthService appleOauthService;

    @Operation(summary = "Kakao 인증", description = "Kakao Access Token으로 이메일 정보를 얻어 회원가입/재로그인 합니다.")
    @Parameter(name = "loginRequestDto", description = "Kakao에서 받은 Access Token")
    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> loginWithKakao(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = kakaoOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.KAKAO, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @Operation(summary = "Apple 인증", description = "Apple Identity Token으로 이메일 정보를 얻어 회원가입/재로그인 합니다.")
    @Parameter(name = "loginRequestDto", description = "Apple에서 받은 Identity Token")
    @PostMapping("/apple")
    public ResponseEntity<TokenDto> loginWithApple(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = appleOauthService.getEmail(loginRequestDto);
        TokenDto tokenDto = memberService.login(OAuthType.APPLE, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @Operation(summary = "JWT 토큰 재발급", description = "Access Token의 만료 기한이 지났을 때, 재로그인합니다.")
    @Parameter(name = "tokenDto", description = "Access Token은 만료기한 지났고, Refresh Token은 유효한 상태")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueAccessToken(
            @RequestBody TokenDto tokenDto
    ) {
        TokenDto newTokenDto = memberService.reissueAccessToken(tokenDto);
        return new ResponseEntity(newTokenDto, HttpStatus.OK);
    }

    @Operation(summary = "테스트 인증", description = "이메일만으로 로그인하기")
    @Parameter(name = "loginRequestDto", description = "예시 이메일")
    @PostMapping("/test/login")
    public ResponseEntity<TokenDto> testLogin(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        String email = loginRequestDto.getToken();
        TokenDto tokenDto = memberService.login(OAuthType.APPLE, email);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity("Welcome to Pyonsnal Color", HttpStatus.OK);
    }
}