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
            Sort.by("updatedTime").descending().and(Sort.by("id")),
            Comparator.comparing(BaseProduct::getUpdatedTime).reversed().thenComparing(BaseProduct::getId)),
    VIEW(2,
            "조회순",
            Sort.by("id"),
            Comparator.comparing(BaseProduct::getId)),  // TODO: 이후 추가
    LOW_PRICE(3,
            "가격낮은순",
            Sort.by("priceInt").ascending().and(Sort.by("id")),
            Comparator.comparing(BaseProduct::getPriceInt).thenComparing(BaseProduct::getId)),
    HIGH_PRICE(4,
            "가격높은순",
            Sort.by("priceInt").descending().and(Sort.by("id")),
            Comparator.comparing(BaseProduct::getPriceInt).reversed().thenComparing(BaseProduct::getId)),
    REVIEW(5,
            "리뷰순",
            Sort.by("id"),
            Comparator.comparing(BaseProduct::getId)); // TODO: 이후 추가

    private final int code;
    private final String korean;
    private final Sort sort;
    private final Comparator<BaseProduct> comparator;

    private static final String FILTER_TYPE = "sort";

    Sorted(int code, String korean, Sort sort, Comparator<BaseProduct> comparator) {
        this.code = code;
        this.korean = korean;
        this.sort = sort;
        this.comparator = comparator;
    }

    public static Sort findSortedByFilterList(List<Integer> filterList) {
        return filterList.stream()
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

    public static Comparator<BaseProduct> getCategoryFilteredComparator(List<Integer> filterList) {
        Comparator<BaseProduct> filter = Sorted.findComparatorByFilterList(filterList);

        if (Sorted.LATEST.getComparator() == filter) {
            return BaseProduct.getCategoryComparator()
                    .thenComparing(filter);
        }
        return filter;
    }

    private static Comparator<BaseProduct> findComparatorByFilterList(List<Integer> filterList) {
        return filterList.stream()
                .map(Sorted::matchSortedByCode)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(LATEST)
                .getComparator();
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