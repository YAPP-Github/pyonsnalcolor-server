package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.entity.BaseProduct;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
public enum Sorted implements Filter {
    LATEST(1,
            "최신순",
            null,
            Sort.by("updatedTime").descending().and(Sort.by("id").ascending()),
            Comparator.comparing(BaseProduct::getCreatedDate).reversed().thenComparing(BaseProduct::getId)),
//    VIEW(2,
//            "조회순",
//            null,
//            Comparator.comparing(BaseProduct::getId)),  // TODO: 이후 추가
    LOW_PRICE(3,
            "낮은가격순",
            null,
            Sort.by("price").ascending().and(Sort.by("id").ascending()),
            Comparator.comparing(BaseProduct::getPrice).thenComparing(BaseProduct::getId)),
    HIGH_PRICE(4,
            "높은가격순",
            null,
            Sort.by("price").descending().and(Sort.by("id").ascending()),
            Comparator.comparing(BaseProduct::getPrice).reversed().thenComparing(BaseProduct::getId));
//    REVIEW(5,
//            "리뷰순",
//            null,
//            Comparator.comparing(BaseProduct::getId)); // TODO: 이후 추가

    private final int code;
    private final String korean;
    private final String image;
    private final Sort sort;
    private final Comparator<BaseProduct> comparator;

    private static final String FILTER_TYPE = "sort";

    Sorted(int code, String korean, String image, Sort sort, Comparator<BaseProduct> comparator) {
        this.code = code;
        this.korean = korean;
        this.image = image;
        this.sort = sort;
        this.comparator = comparator;
    }

    private static Sorted matchSortedByCode(int code) {
        return Arrays.stream(values())
                .filter(t -> t.code == code)
                .findFirst()
                .orElse(null);
    }

    public static Comparator<BaseProduct> findComparatorByFilterList(List<Integer> filterList) {
        return filterList.stream()
                .map(Sorted::matchSortedByCode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(LATEST)
                .getComparator();
    }

    public static Sort findSortByFilterList(List<Integer> filterList) {
        return filterList.stream()
                .map(Sorted::matchSortedByCode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(LATEST)
                .getSort();
    }

    @Override
    public String getFilterType() {
        return FILTER_TYPE;
    }

    @Override
    public List<String> getKeywords() {
        return null; // 필요X
    }
}