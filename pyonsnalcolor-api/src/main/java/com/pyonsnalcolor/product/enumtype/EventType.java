package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.dto.MetaDataDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EventType {
    ONE_TO_ONE(10001L),
    TWO_TO_ONE(10002L),
    THREE_TO_ONE(10003L),
    GIFT(10004L),
    DISCOUNT(10005L);

    private final Long code;

    EventType(Long code) {
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
}
