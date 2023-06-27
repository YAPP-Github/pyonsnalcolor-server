package com.pyonsnalcolor.client;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GS25EventRequestBody {
    private int pageNum=1;
    private int pageSize=8;
    private String searchType;
    private String searchWord;
    private List<String> parameterList = Arrays.asList("TOTAL");

    public void updateNextPage() {
        pageNum++;
    }
}
