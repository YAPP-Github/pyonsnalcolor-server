package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Tag {

    FATIGUE_RECOVER("피로회복", Arrays.asList("비타", "과일", "액티비아", "에너지", "종근당", "인삼", "홍삼", "보약")),
    WINTER("겨울음식", Arrays.asList("우동", "오뎅", "호빵", "북엇국")),
    DIET("다이어트", Arrays.asList("제로", "라이트", "닭가슴살", "무설탕", "그래놀라", "곤약")),
    CHARACTER("캐릭터", Arrays.asList("포켓몬", "짱구", "원피스")),
    SPICY("매운", Arrays.asList("불닭", "사천", "엽떡", "마라", "매콤", "할라피뇨", "떡볶이")),
    HANGOVER("해장", Arrays.asList("헛개", "수프", "국")),
    DRINK_SNACK("안주", Arrays.asList("김부각", "어묵바", "오징어", "젤리", "소시지", "땅콩", "믹스넛",
            "후랑크", "아몬드", "안주", "오뎅", "치즈", "육포")),
    SWEET("달달", Arrays.asList("초코", "초콜릿", "키세스", "허쉬", "킷캣", "쿠앤크", "팥",
            "바닐라", "허니", "빙수", "젤리")),
    ALONE("혼밥", Arrays.asList("김밥", "컵밥", "큰컵")),
    MIDNIGHT_SNACK("야식", Arrays.asList("닭발", "후랑크", "버거", "라면", "샌드위치", "치킨")),
    HUNGRY_SNACKS("요깃거리", Arrays.asList("구운란", "군밤", "소시지", "고구마", "계란", "비엔나")),
    NIGHT_WORK("밤샘", Arrays.asList("카페인", "녹차", "커피", "껌")),
    EXERCISE("운동", Arrays.asList("프로틴", "이온", "단백질", "에너지바"));

    private final String korean;
    private final List<String> keywords;

    Tag(String korean, List<String> keywords) {
        this.korean = korean;
        this.keywords = keywords;
    }

    public static Tag findTag(String name) {
        return Arrays.stream(Tag.values())
                .filter(tag -> tag.getKeywords()
                        .stream()
                        .anyMatch(name::contains))
                .findFirst()
                .orElse(null);
    }
}