package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.metadata.FilterItem;
import com.pyonsnalcolor.product.metadata.FilterItems;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Filter {

    int getCode();

    String getKorean();

    String getFilterType();

    List<String> getKeywords();

    static Criteria getCriteria(List<? extends Filter> filters, String field, Criteria criteria) {
        if (!filters.isEmpty()) {
            return criteria.and(field).in(filters);
        }
        return  criteria;
    }

    static <T extends Filter> List<T> findEnumByFilterList(Class<T> enumClass, List<Integer> filterList) {
        return filterList.stream()
                .map(code -> matchEnumByCode(enumClass, code))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    static <T extends Filter> T matchEnumByCode(Class<T> enumClass, int code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode() == code)
                .findFirst()
                .orElse(null);
    }

    static <T extends Filter> List<FilterItem> getFilterItem(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(filter -> FilterItem.builder()
                        .name(filter.getKorean())
                        .code(filter.getCode())
                        .build())
                .collect(Collectors.toList());
    }

    static <T extends Filter> FilterItems getMetaData(Class<T> enumClass) {
        String filterType = enumClass.getEnumConstants()[0].getFilterType();
        return new FilterItems(filterType, getFilterItem(enumClass));
    }

    static <T extends Filter> T matchEnumTypeByProductName(Class<T> enumClass, String productName) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(filter -> filter.getKeywords()
                        .stream()
                        .anyMatch(productName::contains))
                .findFirst()
                .orElse(null);
    }

    static <T extends Filter> List<Integer> getCodes(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Filter::getCode)
                .collect(Collectors.toUnmodifiableList());
    }
}