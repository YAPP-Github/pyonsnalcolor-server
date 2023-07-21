package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.dto.MetaDataDto;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Sorted {
    LATEST(1, "최신순", Sort.by("updatedTime").descending()),
    VIEW(2, "조회순", Sort.by("updatedTime")),
    LOW_PRICE(3, "가격낮은순", Sort.by("price").ascending()),
    HIGH_PRICE(4, "가격높은순", Sort.by("price").descending()),
    REVIEW(5, "리뷰순", Sort.by("updatedTime"));

    public static final int DIVIDER = 10;

    private final int code;
    private final String korean;
    private final Sort sort;

    Sorted(int code, String korean, Sort sort) {
        this.code = code;
        this.korean = korean;
        this.sort = sort;
    }

    public static List<MetaDataDto> getSortedWithCodes() {

        return Arrays.stream(Sorted.values())
                .map(sorted -> MetaDataDto.builder()
                        .name(sorted.korean)
                        .code(sorted.code)
                        .build())
                .collect(Collectors.toList());
    }

    public static Sort findSortByCode(int code) {

        return Arrays.stream(Sorted.values())
                .filter(sorted -> sorted.code == code)
                .findFirst()
                .map(Sorted::getSort)
                .orElseThrow();
    }
}