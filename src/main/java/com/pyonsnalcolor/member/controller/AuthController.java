package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.member.dto.*;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 인증/인가용 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    @Operation(summary = "OAuth 인증", description = "Token으로 이메일 정보를 얻어 회원가입/재로그인합니다.")
    @Parameter(name = "loginRequestDto", description = "OAuth Token과 OAuth 타입(Apple, Kakao)")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        LoginResponseDto loginResponseDto = authService.oAuthLogin(loginRequestDto);
        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "회원가입 여부 조회", description = "이미 가입된 회원인지 조회합니다.")
    @Parameter(name = "loginRequestDto", description = "OAuth Token과 OAuth 타입")
    @PostMapping("/status")
    public ResponseEntity<JoinStatusResponseDto> getJoinStatus(
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        JoinStatusResponseDto joinStatusResponseDto = authService.getJoinStatus(loginRequestDto);
        return new ResponseEntity(joinStatusResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "JWT 토큰 재발급", description = "Access Token의 만료 기한이 지났을 때, 재로그인합니다.")
    @Parameter(name = "tokenDto", description = "Access Token은 만료기한 지났고, Refresh Token은 유효한 상태")
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponseDto> reissueAccessToken(
            @RequestBody TokenDto tokenRequestDto
    ) {
        LoginResponseDto tokenResponseDto = authService.reissueAccessToken(tokenRequestDto);
        return new ResponseEntity(tokenResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "테스트 로그인/회원가입", description = "Token으로 이메일 정보를 얻어 회원가입/재로그인합니다.")
    @PostMapping("/test/login")
    public ResponseEntity<LoginResponseDto> testLogin(
            @RequestParam OAuthType oAuthType,
            @RequestParam String email
    ) {
        LoginResponseDto loginResponseDto = authService.join(oAuthType, email);
        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }
}
