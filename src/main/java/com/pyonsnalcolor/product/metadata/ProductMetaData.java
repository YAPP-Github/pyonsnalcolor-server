package com.pyonsnalcolor.product.metadata;

import com.pyonsnalcolor.product.enumtype.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductMetaData {

    private static final String META_DATA_KEY = "data";

    private static ProductMetaData instance;
    private final List<FilterItems> metaDataList;

    private ProductMetaData() {
        metaDataList = List.of(
                Filter.getMetaData(Sorted.class),
                Filter.getMetaData(Recommend.class),
                Filter.getMetaData(Category.class),
                Filter.getMetaData(EventType.class)
        );
    }

    public static ProductMetaData getInstance() {
        if (instance == null) {
            instance = new ProductMetaData();
        }
        return instance;
    }

    public Map<String, List<FilterItems>> getMetadata() {
        return Collections.singletonMap(META_DATA_KEY, metaDataList);
    }
}