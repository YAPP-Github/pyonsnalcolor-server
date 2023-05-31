package com.pyonsnalcolor.batch.model;

import lombok.Getter;

@Getter
public enum EventType {
    ONE_TO_ONE("ONE_TO_ONE"),
    TWO_TO_ONE("TWO_TO_ONE"),
    THREE_TO_ONE("THREE_TO_ONE"),
    GIFT("GIFT");

    private String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType getEventTypeWithValue(String value) {
        for (EventType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
