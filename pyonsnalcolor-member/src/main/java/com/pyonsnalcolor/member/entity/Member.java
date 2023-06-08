package com.pyonsnalcolor.member.entity;

import com.pyonsnalcolor.member.entity.enumtype.LoginType;
import com.pyonsnalcolor.member.entity.enumtype.Role;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oauthId; // {Oauth 타입 + 사용자 정보}

    private String email;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;
}