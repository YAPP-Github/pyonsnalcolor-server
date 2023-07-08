package com.pyonsnalcolor.product.entity;

import java.util.UUID;

public class UUIDGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
