package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.enumtype.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Like taste; //맛
    private Like quality; //퀄리티
    private Like valueForMoney; //가성비
    private float score; //별점
    private String contents; //내용
    private String image; //이미지
}
