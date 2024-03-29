package com.pyonsnalcolor.member.security;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.exception.model.AuthErrorCode;
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
                .orElseThrow(() -> new PyonsnalcolorAuthException(AuthErrorCode.INVALID_OAUTH_ID));

        return new AuthUserDetails(member);
    }
}
