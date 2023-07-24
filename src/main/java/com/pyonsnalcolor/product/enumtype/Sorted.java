package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.metadata.FilterItems;
import com.pyonsnalcolor.product.metadata.FilterItem;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum Sorted {
    LATEST(1, "최신순", Sort.by("updatedTime").descending().and(Sort.by("id"))),
    VIEW(2, "조회순", Sort.by("updatedTime").descending().and(Sort.by("id"))),  // TODO: 이후 추가
    LOW_PRICE(3, "가격낮은순", Sort.by("price").ascending().descending().and(Sort.by("id"))),
    HIGH_PRICE(4, "가격높은순", Sort.by("price").descending().descending().and(Sort.by("id"))),
    REVIEW(5, "리뷰순", Sort.by("updatedTime").descending().and(Sort.by("id"))); // TODO: 이후 추가

    private final int code;
    private final String korean;
    private final Sort sort;

    public static FilterItems sortedMetaData = new FilterItems("sort", getSortedMetaData());

    Sorted(int code, String korean, Sort sort) {
        this.code = code;
        this.korean = korean;
        this.sort = sort;
    }

    private static List<FilterItem> getSortedMetaData() {
        return Arrays.stream(values())
                .map(sorted -> FilterItem.builder()
                        .name(sorted.korean)
                        .code(sorted.code)
                        .build())
                .collect(Collectors.toList());
    }

    public static Sort findSortedByFilterList(String filterList) {
        return Arrays.stream(filterList.split(","))
                .map(Integer::parseInt)
                .map(Sorted::matchSortedByCode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(LATEST)
                .getSort();
    }

    private static Sorted matchSortedByCode(int code) {
        return Arrays.stream(values())
                .filter(t -> t.code == code)
                .findFirst()
                .orElse(null);
    }
}