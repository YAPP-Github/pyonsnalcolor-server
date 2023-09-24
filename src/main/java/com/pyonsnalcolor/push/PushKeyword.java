package com.pyonsnalcolor.push;

import com.pyonsnalcolor.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "push_keyword")
public class PushKeyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_keyword_id")
    private Long id;

    @Pattern(regexp="^[0-9a-zA-Zㄱ-ㅎ가-힣]{1,10}")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
