package com.pyonsnalcolor.product.metadata;

import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.Recommend;
import com.pyonsnalcolor.product.enumtype.Sorted;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductMetaData {

    private static final String META_DATA_KEY = "data";

    private static ProductMetaData instance;
    private List<FilterItems> metaDataList;

    private ProductMetaData() {
        metaDataList = List.of(Sorted.sortedMetaData,
                        Recommend.recommendMetaData,
                        Category.categoryMetaData,
                        EventType.eventTypeMetaData);
    }

    public static ProductMetaData getInstance() {
        if (instance == null) {
            instance = new ProductMetaData();
        }
        return instance;
    }

    public Map<String, List<FilterItems>> getMetadataList() {
        return Collections.singletonMap(META_DATA_KEY, metaDataList);
    }
}