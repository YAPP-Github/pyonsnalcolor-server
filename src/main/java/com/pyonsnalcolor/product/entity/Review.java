package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.enumtype.Like;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String reviewId;
    private Like taste; //맛
    private Like quality; //퀄리티
    private Like valueForMoney; //가성비
    private float score; //별점
    private String contents; //내용
    private String image; //이미지
    private Long writerId; // 작성자 id <- 이후 기능 추가 고려
    private String writerName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LikeCount likeCount;
    private HateCount hateCount;

    public ReviewDto convertToDto() {
        return new ReviewDto(reviewId, taste, quality, valueForMoney, score, contents, image, writerId, writerName,
                createdTime, updatedTime, likeCount, hateCount);
    }

    public void likeReview(Long writerId) {
        if (this.likeCount == null) {
            this.likeCount = new LikeCount();
        }
        if (this.likeCount.getWriterIds().contains(writerId)) {
            return;
        }
        this.likeCount.addWriter(writerId);
        this.likeCount.setLikeCount(this.likeCount.getLikeCount() + 1);

        if (this.hateCount != null && this.hateCount.getWriterIds().contains(writerId)) {
            this.hateCount.setHateCount(this.hateCount.getHateCount() - 1);
            this.hateCount.getWriterIds().remove(writerId);
        }
    }

    public void hateReview(Long writerId) {
        if (this.hateCount == null) {
            this.hateCount = new HateCount();
        }
        if (this.hateCount.getWriterIds().contains(writerId)) {
            return;
        }
        this.hateCount.addWriter(writerId);
        this.hateCount.setHateCount(this.hateCount.getHateCount() + 1);

        if (this.likeCount != null && this.likeCount.getWriterIds().contains(writerId)) {
            this.likeCount.setLikeCount(this.likeCount.getLikeCount() - 1);
            this.likeCount.getWriterIds().remove(writerId);
        }
    }
}
