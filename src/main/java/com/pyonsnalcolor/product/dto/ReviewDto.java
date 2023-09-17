package com.pyonsnalcolor.product.dto;

import com.pyonsnalcolor.product.enumtype.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Like taste; //맛
    private Like quality; //퀄리티
    private Like valueForMoney; //가성비
    private float score; //별점
    private String contents; //내용
    private String image;
    private Long writerId; //작성자 <- 이후 기능 확장 고려한 필드
    private String writerName; // 닉네임
}
