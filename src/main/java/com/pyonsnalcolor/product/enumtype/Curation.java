package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.List;

@Getter
public enum Curation implements Filter {

    SNS("SNS 추천 상품✨", null, "인플루언서들이 추천하는 편의점별 단독 상품"),
    SEASON("무더운 여름을 이겨낼⛱", null, "더운 여름에 추천하는 편의점별 단독 상품"),
    STEADY_SELLER("올해의 스테디셀러\uD83D\uDC98", null, "꾸준히 사랑받고 있는 편의점별 단독 상품");

    private final String korean;
    private final String image;
    private final String description;

    Curation(String korean, String image, String description) {
        this.korean = korean;
        this.image = image;
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