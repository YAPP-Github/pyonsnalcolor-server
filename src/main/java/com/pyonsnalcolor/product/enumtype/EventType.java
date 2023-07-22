package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.dto.MetaDataDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EventType {
    ONE_TO_ONE(10001),
    TWO_TO_ONE(10002),
    THREE_TO_ONE(10003),
    GIFT(10004),
    DISCOUNT(10005);

    public static final int DIVIDER = 100000;
    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public static List<MetaDataDto> getEventWithCodes() {

        return Arrays.stream(EventType.values())
                .map(event -> MetaDataDto.builder()
                        .name(event.name())
                        .code(event.code)
                        .build())
                .collect(Collectors.toList());
    }

    public static EventType findEventByCode(int code) {

        return Arrays.stream(EventType.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow();
    }
}