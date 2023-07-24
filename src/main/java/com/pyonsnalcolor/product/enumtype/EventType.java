package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.metadata.FilterItems;
import com.pyonsnalcolor.product.metadata.FilterItem;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum EventType {
    ONE_TO_ONE(10001),
    TWO_TO_ONE(10002),
    THREE_TO_ONE(10003),
    GIFT(10004),
    DISCOUNT(10005),
    NONE(10006);

    private final int code;

    public static FilterItems eventTypeMetaData = new FilterItems("event", getEventMetaData());

    EventType(int code) {
        this.code = code;
    }

    private static List<FilterItem> getEventMetaData() {
        return Arrays.stream(values())
                .map(event -> FilterItem.builder()
                        .name(event.name())
                        .code(event.code)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<EventType> findEventTypeByFilterList(String filterList) {
        return Arrays.stream(filterList.split(","))
                .map(Integer::parseInt)
                .map(EventType::matchEventTypeByCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static EventType matchEventTypeByCode(int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElse(null);
    }
}