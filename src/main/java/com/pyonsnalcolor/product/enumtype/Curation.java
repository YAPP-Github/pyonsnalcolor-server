package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.List;

@Getter
public enum Curation implements Filter {

    SNS("SNS 인기상품", "SNS에서 인기있는 편의점별 단독 상품"),
    SEASON("계절추천", "올 여름 추천 상품"),
    STEADY_SELLER("올해의 스테디셀러", "올해 사랑받고 있는 편의점 상품");

    private final String korean;
    private final String description;

    Curation(String korean, String description) {
        this.korean = korean;
        this.description = description;
    }

    @Override
    public int getCode() {
        return 0; // 여기선 필요 X
    }

    @Override
    public String getFilterType() {
        return this.getDeclaringClass().getSimpleName().toLowerCase();
    }

    @Override
    public List<String> getKeywords() {
        return null; // 여기선 필요 X
    }
}