package com.pyonsnalcolor.domain.product;

import java.util.UUID;

public class UUIDGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
