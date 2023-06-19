package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueAccessToken(Authentication authentication) {
        TokenDto newTokenDto = memberService.reissueAccessToken(authentication);
        return new ResponseEntity(newTokenDto, HttpStatus.OK);
    }

    @GetMapping("/infos")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(Authentication authentication) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(authentication);
        return new ResponseEntity(memberInfoResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<TokenDto> updateNickname(
            Authentication authentication,
            @RequestBody NicknameRequestDto nicknameRequestDto
    ) {
        memberService.updateNickname(authentication, nicknameRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<TokenDto> withdraw(Authentication authentication) {
        memberService.withdraw(authentication);
        return new ResponseEntity(HttpStatus.OK);
    }
}