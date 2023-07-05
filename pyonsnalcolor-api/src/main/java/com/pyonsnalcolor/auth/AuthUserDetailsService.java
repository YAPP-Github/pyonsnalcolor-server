package com.pyonsnalcolor.auth;

import com.pyonsnalcolor.exception.AuthException;
import com.pyonsnalcolor.exception.model.AuthErrorCode;
import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByoAuthId(username)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_OAUTH_ID));

        return new AuthUserDetails(member);
    }
}