package com.pyonsnalcolor.product.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HateCount {
    private List<Long> writerIds = new ArrayList<>();
    private Long hateCount = 0L;

    public void addWriter(Long writerId) {
        this.writerIds.add(writerId);
    }
}
