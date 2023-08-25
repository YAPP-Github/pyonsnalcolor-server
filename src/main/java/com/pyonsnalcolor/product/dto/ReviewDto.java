package com.pyonsnalcolor.product.dto;

import com.pyonsnalcolor.product.enumtype.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewDto {
    private Like taste; //맛
    private Like quality; //퀄리티
    private Like valueForMoney; //가성비
    private float score; //별점
    private String contents; //내용
}
