package com.pyonsnalcolor.client;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GS25PbRequestBody {
    private int pageNum=1;
    private int pageSize=8;
    private String searchWord;
    private String searchHPrice;
    private String searchTPrice;
    private String searchSrvFoodCK = "DifferentServiceKey";
    private String searchSort = "searchALLSort";
    private String searchProduct = "productALL";

    public void updateNextPage() {
        pageNum++;
    }
}
