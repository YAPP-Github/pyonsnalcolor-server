package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.domain.member.Member;
import com.pyonsnalcolor.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByOauthId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 사용자가 없습니다."));
        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().toString()));

        return new User(
                member.getOauthId(),
                "",
                grantedAuthorities);
    }
}