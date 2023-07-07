package com.pyonsnalcolor.push;

import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "push_record")
public class PushRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_record_id")
    private Long id;

    private String title;

    private String body;

    private StoreType storeType;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
