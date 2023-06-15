package com.pyonsnalcolor.domain.alarm;

import com.pyonsnalcolor.domain.alarm.enumtype.StoreFcmTopic;
import com.pyonsnalcolor.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "fcm_topic")
public class FcmTopic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_topic_id")
    private Long id;

    private StoreFcmTopic storeFcmTopic;

    @Column(name = "is_subscribed")
    private boolean isSubscribed;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
