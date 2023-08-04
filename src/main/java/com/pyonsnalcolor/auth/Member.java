package com.pyonsnalcolor.auth;

import com.pyonsnalcolor.auth.enumtype.OAuthType;
import com.pyonsnalcolor.auth.enumtype.Role;
import com.pyonsnalcolor.product.entity.BaseTimeEntity;
import com.pyonsnalcolor.push.PushKeyword;
import com.pyonsnalcolor.push.PushProductStore;
import com.pyonsnalcolor.push.PushRecord;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String oAuthId; // {Oauth 타입 + 사용자 정보}

    @Email
    private String email;

    @Pattern(regexp="^[0-9a-zA-Zㄱ-ㅎ가-힣 ]{1,15}")
    private String nickname;

    @Column(length = 500)
    private String refreshToken;

    private String deviceToken; // FCM용 디바이스 토큰

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthType OAuthType;

    @OneToMany(mappedBy = "member")
    private List<PushProductStore> pushProductStores = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PushKeyword> pushKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PushRecord> pushRecords = new ArrayList<>();

    public void updateNickname(String updatedNickname) {
        this.nickname = updatedNickname;
    }
}
