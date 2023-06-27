package com.pyonsnalcolor.auth.controller;

import com.pyonsnalcolor.auth.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.auth.dto.NicknameRequestDto;
import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.auth.CustomUserDetails;
import com.pyonsnalcolor.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenDto tokenDto) {
        memberService.logout(tokenDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/infos")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(customUserDetails);
        return new ResponseEntity(memberInfoResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<TokenDto> updateNickname(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid NicknameRequestDto nicknameRequestDto
    ) {
        memberService.updateNickname(customUserDetails, nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<TokenDto> withdraw(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        memberService.withdraw(customUserDetails);
        return new ResponseEntity(HttpStatus.OK);
    }
}