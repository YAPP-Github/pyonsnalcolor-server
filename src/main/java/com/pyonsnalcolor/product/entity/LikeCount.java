package com.pyonsnalcolor.product.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LikeCount {
    private List<Long> writerIds = new ArrayList<>();
    private Long likeCount = 0L;

    public void addWriter(Long writerId) {
        this.writerIds.add(writerId);
    }
}
