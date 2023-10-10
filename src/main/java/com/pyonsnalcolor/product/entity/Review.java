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
    private Long likeCount;
    private Long hateCount;

    public ReviewDto convertToDto() {
        return new ReviewDto(taste, quality, valueForMoney, score, contents, image, writerId, writerName,
                createdTime, updatedTime, likeCount, hateCount);
    }

    public void likeReview() {
        if(this.likeCount == null) {
            this.likeCount = 0L;
        }
        this.likeCount += 1;
    }

    public void hateReview() {
        if(this.hateCount == null) {
            this.hateCount = 0L;
        }
        this.hateCount += 1;
    }
}
