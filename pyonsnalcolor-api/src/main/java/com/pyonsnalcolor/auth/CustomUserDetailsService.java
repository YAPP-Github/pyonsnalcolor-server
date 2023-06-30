package com.pyonsnalcolor.auth;

import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByoAuthId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 oAuthId을 가진 사용자가 없습니다."));

        return new CustomUserDetails(member);
    }
}