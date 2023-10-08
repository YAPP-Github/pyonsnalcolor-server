package com.pyonsnalcolor.push;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.product.enumtype.ProductStoreType;
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

    @Column(name = "product_store_type")
    @Enumerated(EnumType.STRING)
    private ProductStoreType productStoreType;

    @Column(name = "is_subscribed")
    @ColumnDefault("true")
    private boolean isSubscribed;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateSubscribedStatus(boolean updatedStatus) {
        isSubscribed = updatedStatus;
    }
}
