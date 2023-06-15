package com.pyonsnalcolor.domain.member;

import com.pyonsnalcolor.domain.alarm.FcmTopic;
import com.pyonsnalcolor.domain.alarm.PushKeyword;
import com.pyonsnalcolor.domain.alarm.PushRecord;
import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.domain.member.enumtype.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oauthId; // {Oauth 타입 + 사용자 정보}

    private String email;

    private String nickname;

    private String refreshToken;

    private String deviceToken; // FCM용 디바이스 토큰

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthType OAuthType;

    @OneToMany(mappedBy = "member")
    private List<FcmTopic> fcmTopics = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PushKeyword> pushKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PushRecord> pushRecords = new ArrayList<>();
}