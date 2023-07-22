package com.pyonsnalcolor.batch.client;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GS25PromotionRequestBody {
    private int pageNum=1;
    private int pageSize=10;
    private String modelName = "event";
    private List<String> parameterList = Arrays.asList("brandCode:GS25@!@eventFlag:CURRENT");

    public void updateNextPage() {
        pageNum++;
    }
}
