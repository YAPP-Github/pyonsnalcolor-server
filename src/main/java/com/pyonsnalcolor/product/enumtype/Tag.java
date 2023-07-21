package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.dto.MetaDataDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Tag {

    NIGHT_WORK(101, "밤샘", Arrays.asList("비타", "과일", "액티비아", "에너지", "종근당", "인삼", "홍삼",
            "보약", "카페인", "녹차", "커피", "껌")),
    DIET(102, "다이어트", Arrays.asList("제로", "라이트", "닭가슴살", "무설탕", "그래놀라", "곤약")),
    CHARACTER(103, "캐릭터", Arrays.asList("포켓몬", "짱구", "원피스")),
    SPICY(104, "매운", Arrays.asList("불닭", "사천", "엽떡", "마라", "매콤", "할라피뇨", "떡볶이")),
    DRINK_SNACK(105, "간식", Arrays.asList("김부각", "어묵바", "오징어", "젤리", "소시지", "땅콩", "믹스넛",
            "후랑크", "아몬드", "안주", "오뎅", "치즈", "육포")),
    SWEET(106, "달달", Arrays.asList("초코", "초콜릿", "키세스", "허쉬", "킷캣", "쿠앤크", "팥",
            "바닐라", "허니", "빙수", "젤리")),
    ALONE(107, "혼밥", Arrays.asList("김밥", "컵밥", "큰컵")),
    POPULAR(108, "인기있는", Arrays.asList());
    // TODO: 이후에 추가될 항목들
    // MIDNIGHT_SNACK(code, "야식", Arrays.asList("닭발", "후랑크", "버거", "라면", "샌드위치", "치킨")),
    // HANGOVER(code, "해장", Arrays.asList("헛개", "수프", "국", "우동", "오뎅", "호빵", "북엇국")),
    // HUNGRY_SNACKS("요깃거리", Arrays.asList("구운란", "군밤", "소시지", "고구마", "계란", "비엔나")),
    // NIGHT_WORK("밤샘", Arrays.asList("카페인", "녹차", "커피", "껌")),
    // EXERCISE("운동", Arrays.asList("프로틴", "이온", "단백질", "에너지바"));

    public static final int DIVIDER = 1000;

    private final int code;
    private final String korean;
    private final List<String> keywords;

    Tag(int code, String korean, List<String> keywords) {
        this.code = code;
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

    public static List<MetaDataDto> getTagWithCodes() {

        return Arrays.stream(Tag.values())
                .map(tag -> MetaDataDto.builder()
                        .name(tag.korean)
                        .code(tag.code)
                        .build())
                .collect(Collectors.toList());
    }
}