package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.entity.CustomUserDetails;
import com.pyonsnalcolor.member.service.MemberService;
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

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueAccessToken(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws Exception {
        TokenDto newTokenDto = memberService.reissueAccessToken(customUserDetails);
        return new ResponseEntity(newTokenDto, HttpStatus.OK);
    }

    @GetMapping("/infos")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws Exception {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(customUserDetails);
        return new ResponseEntity(memberInfoResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<TokenDto> updateNickname(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid NicknameRequestDto nicknameRequestDto
    ) throws Exception {
        memberService.updateNickname(customUserDetails, nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<TokenDto> withdraw(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws Exception {
        memberService.withdraw(customUserDetails);
        return new ResponseEntity(HttpStatus.OK);
    }
}