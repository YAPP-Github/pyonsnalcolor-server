package com.pyonsnalcolor.domain.alarm;

import com.pyonsnalcolor.domain.alarm.enumtype.ProductStoreType;
import com.pyonsnalcolor.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "push_product_store")
public class PushProductStore {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_product_store_id")
    private Long id;

    private ProductStoreType productStoreType;

    @Column(name = "is_subscribed")
    @ColumnDefault("true")
    private boolean isSubscribed;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}