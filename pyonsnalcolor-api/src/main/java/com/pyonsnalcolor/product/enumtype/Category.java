package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Category {

    GOODS("생활용품", Arrays.asList("세제", "샴푸", "린스", "페브리즈", "좋은", "치약", "칫솔", "극세모", "미세모",
            "클렌징폼", "면도", "대형", "중형", "소형", "실내", "클렌징", "오일", "피죤", "유기농", "로션", "크림", "립밤", "티슈",
            "바디", "쏘피", "스프레이", "니베아", "마스크", "샤프란", "가그린", "가글", "주방")),
    CONVENIENCE_FOOD("간편식사", Arrays.asList("그래놀라", "시리얼", "컵밥", "교자", "사발",
            "큰컵", "소컵", "라면", "수프", "닭가슴살", "도시락", "떡볶이", "햇반", "버거", "호빵"
    )),
    FOOD("식품", Arrays.asList("오뚜기", "비비고", "만두", "김치", "교자", "탕", "양념", "우동", "육즙", "스파게티", "스팸",
            "쫄면", "참치", "리챔", "국", "라멘", "파스타", "양념", "떡볶이", "오뚜기", "밥", "죽", "김", "미식"
    )),
    DRINK("음료", Arrays.asList("ml", "두유", "제로", "나랑드", "병", "매일", "헛개", "라떼", "밀크티", "아메", "덴마크)",
            "우유", "요구르트", "얼그레이", "밀키스", "홍차", "차", "쌍화", "콤부차", "남양", "밀크", "커피", "할리스", "에이드"
    )),
    SNACK("과자", Arrays.asList("파이", "오레오", "와플", "꼬깔콘", "산도", "캔디", "썬", "쿠키", "맛동산", "카라멜",
            "비스켓", "로아커", "감자", "스낵", "해태", "껌", "샌드", "사탕", "젤리", "캔디", "비요뜨", "크래커", "양갱", "오리온")),
    ICE_CREAM("아이스크림", Arrays.asList("파르페", "탱크보이", "슈퍼콘", "월드콘", "우유콘", "설레임", "아이스", "하겐다즈",
            "부라보콘", "메로나", "죠스바", "구구콘", "수박바", "비비빅", "쌍쌍바", "쉐이크", "스크류바", "옥동자", "찰옥수수",
            "더블비얀코", "보석바", "돼지바", "빠삐코", "빵빠레")),
    BAKERY("베이커리", Arrays.asList("케익", "빵", "카스테라", "도넛", "마들렌"));

    private final String korean;
    private final List<String> keywords;

    Category(String korean, List<String> keywords) {
        this.korean = korean;
        this.keywords = keywords;
    }

    public static Category matchCategoryByProductName(String name) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getKeywords()
                        .stream()
                        .anyMatch(name::contains))
                .findFirst()
                .orElse(null);
    }
//
//    public static Category getCategory(String categoryStr) {
//        return Arrays.stream(Category.values())
//                .filter(c -> c.korean.equals(categoryStr))
//                .findFirst()
//                .orElse(null);
//    }
}