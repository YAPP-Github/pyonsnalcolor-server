package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.exception.model.CommonErrorCode;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class PbProductService extends ProductService {

    private static final String CATEGORY_GOODS_FIELD = "categoryGoods";
    private static final List<Integer> FILTER_CODES = Stream.of(
            Filter.getCodes(Category.class),
            Filter.getCodes(Sorted.class),
            Filter.getCodes(Recommend.class),
            Filter.getCodes(EventType.class))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

    public PbProductService(PbProductRepository pbProductRepository, MongoTemplate mongoTemplate) {
        super(pbProductRepository, mongoTemplate);
    }

    @Override
    protected void validateProductFilterCodes(List<Integer> filterList) {
        if (!FILTER_CODES.containsAll(filterList)) {
            throw new PyonsnalcolorProductException(CommonErrorCode.INVALID_FILTER_CODE);
        }
    }

    @Override
    protected Page<BasePbProduct> getPagedProductsByFilter(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        Aggregation aggregation = getAggregationBySortCondition(pageable, storeType, productFilterRequestDto);

        AggregationResults<BasePbProduct> aggregationResults = mongoTemplate.aggregate(
                aggregation, "pb_product", BasePbProduct.class
        );

        return PageableExecutionUtils.getPage(
                aggregationResults.getMappedResults(),
                pageable,
                () -> mongoTemplate.count(new Query(), BasePbProduct.class)
        );
    }

    private Aggregation getAggregationBySortCondition(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        Sort sort = Sorted.findSortByFilterList(productFilterRequestDto.getFilterList());

        if (sort == Sorted.LATEST.getSort()) {
            return getAggregationSortCategoryOfGoodsLast(pageable, storeType, productFilterRequestDto);
        } return getAggregation(pageable, storeType, productFilterRequestDto);
    }

    private Aggregation getAggregationSortCategoryOfGoodsLast(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        List<Integer> filterList = productFilterRequestDto.getFilterList();
        Criteria criteria = createCriteriaByFilter(storeType, filterList);
        Sort sort = Sorted.findSortByFilterList(filterList);

        return newAggregation(
                match(criteria),
                addGoodsCategoryField(),
                sort(Sort.Direction.ASC, CATEGORY_GOODS_FIELD),
                project().andExclude(CATEGORY_GOODS_FIELD),
                sort(sort),
                skip(pageable.getOffset()),
                limit(pageable.getPageSize())
        );
    }

    private AggregationOperation addGoodsCategoryField() {
        return addFields()
                .addField(CATEGORY_GOODS_FIELD)
                .withValue(
                        ConditionalOperators.when(
                                ComparisonOperators.Eq.valueOf("category").equalToValue("GOODS"))
                                .then(1)
                                .otherwise(0))
                .build();
    }
}