package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.metadata.FilterItems;
import com.pyonsnalcolor.product.metadata.FilterItem;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum Category {

    GOODS(1001, "생활용품", Arrays.asList("세제", "샴푸", "린스", "페브리즈", "좋은", "치약", "칫솔", "극세모",
            "미세모", "클렌징폼", "면도", "대형", "중형", "소형", "실내", "클렌징", "오일", "피죤", "유기농", "로션",
            "립밤", "밴드", "위생", "백", "장갑", "스푼", "포크", "면봉", "화장", "샤워", "순면", "수세미", "면도", "일회용",
            "티슈", "바디", "쏘피", "스프레이", "니베아", "마스크", "샤프란", "가그린", "가글", "주방", "스푼", "TV", "타월",
            "스타킹", "여행", "비비안", "멀티탭", "노트", "테이프", "매트", "봉투", "A4", "친환경", "양말", "타이즈", "원니즈",
            "유심", "아텍스", "바세린", "USB", "청소", "칼", "우의", "슬리퍼", "미용", "랩", "카드", "배터리", "우산", "다회용",
            "일회용", "충전", "케이블", "손톱", "심리스", "손톱", "발톱", "고급", "AD", "치실", "세트", "비닐", "펜", "은박",
            "삭스", "샤프", "이어폰", "네일", "접착", "여성", "남성", "퍼퓸", "비누", "이쑤시개", "타올", "크린", "젓가락", "집기",
            "쿠션", "일반형", "패치", "귀", "세이퓨", "핫팩", "고속", "타올", "돔", "클린", "아이리버", "잘풀리는집", "종이컵",
            "소주컵", "아우라", "컷터")),
    FOOD(1002, "식품", Arrays.asList("냉동", "햄", "그래놀라", "시리얼", "컵밥", "교자", "사발", "큰컵", "소컵",
            "라면", "수프", "닭가슴살", "도시락", "떡볶이", "햇반", "버거", "호빵", "오뚜기", "비비고", "만두", "김치", "교자",
            "탕", "양념", "우동", "육즙", "스파게티", "스팸", "쫄면", "참치", "리챔", "국", "라멘", "파스타", "양념", "떡볶이",
            "오뚜기", "밥", "죽", "김", "미식", "짜장", "정식","짬뽕", "샐러드", "핫도그", "치킨", "소시지", "소세지", "회",
            "새우", "삼각", "토스트", "마요", "강정", "스테이크", "반찬", "떡", "닭", "비프", "계란", "리조또", "펜네", "도)",
            "훈제", "스프", "바나나", "소세지", "피자", "프랑크", "후랑크", "족발", "삼겹", "고기", "팔도", "천하장사", "구운",
            "당면", "순대", "토끼정", "에그", "육포", "바베큐", "돼지")),
    BAKERY(1006, "베이커리", Arrays.asList("만쥬", "케익", "케이크", "페스츄리", "바게트", "카롱",
            "빵", "카스테라", "도넛", "도너츠", "마들렌", "마카롱","머핀", "브레드", "브라우니", "와플")),
    DRINK(1003, "음료", Arrays.asList("ml", "mL", "L", "두유", "제로", "나랑드", "병", "매일", "헛개", "라떼",
            "밀크티", "아메", "덴마크)", "우유", "요구르트", "얼그레이", "밀키스", "홍차", "차", "쌍화", "콤부차", "남양", "밀크",
            "커피", "주스", "할리스", "에이드", "del", "티", "드링크")),
    SNACK(1004, "과자", Arrays.asList("비스킷", "떠먹는", "초콜릿", "파이", "오레오", "와플", "꼬깔콘", "산도",
            "캔디", "썬", "쿠키", "맛동산", "카라멜", "비스켓", "로아커", "감자", "스낵", "해태", "껌", "사탕", "젤리", "캔디",
            "비요뜨", "크래커", "양갱", "오리온", "버터", "요거트", "크라운", "약과", "팝콘", "과자", "건빵", "칩", "웨하스",
            "나쵸", "웨이퍼", "어니언", "바프")),

    ICE_CREAM(1005, "아이스크림", Arrays.asList("파르페", "탱크보이", "슈퍼콘", "월드콘", "우유콘", "설레임",
            "아이스", "하겐다즈", "부라보콘", "메로나", "죠스바", "구구콘", "수박바", "비비빅", "쌍쌍바", "쉐이크", "스크류바",
            "옥동자", "모나카", "빙수",
            "찰옥수수", "더블비얀코", "보석바", "돼지바", "빠삐코", "빵빠레", "파인트"));

    private final int code;
    private final String korean;
    private final List<String> keywords;

    public static FilterItems categoryMetaData = new FilterItems("category", getCategoryMetaData());

    Category(int code, String korean, List<String> keywords) {
        this.code = code;
        this.korean = korean;
        this.keywords = keywords;
    }

    public static Category matchCategoryByProductName(String name) {
        return Arrays.stream(values())
                .filter(category -> category.getKeywords()
                        .stream()
                        .anyMatch(name::contains))
                .findFirst()
                .orElse(null);
    }

    private static List<FilterItem> getCategoryMetaData() {
        return Arrays.stream(values())
                .map(category -> FilterItem.builder()
                        .name(category.korean)
                        .code(category.code)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Category> findCategoryByFilterList(String filterList) {
        return Arrays.stream(filterList.split(","))
                .map(Integer::parseInt)
                .map(Category::matchCategoryByCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Category matchCategoryByCode(int code) {
        return Arrays.stream(values())
                .filter(c -> c.code == code)
                .findFirst()
                .orElse(null);
    }
}