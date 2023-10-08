package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pyonsnalcolor.exception.model.CommonErrorCode.INVALID_FILTER_CODE;

@Slf4j
@Service
public class EventProductService extends ProductService {

    private static final List<Integer> FILTER_CODES = Stream.of(
                    Filter.getCodes(Category.class),
                    Filter.getCodes(Sorted.class),
                    Filter.getCodes(EventType.class)
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

    public EventProductService(EventProductRepository eventProductRepository,
                               MongoTemplate mongoTemplate,
                               ImageRepository imageRepository) {
        super(eventProductRepository, mongoTemplate, imageRepository);
    }

    @Override
    protected void validateProductFilterCodes(List<Integer> filterList) {
        if (!FILTER_CODES.containsAll(filterList)) {
            throw new PyonsnalcolorProductException(INVALID_FILTER_CODE);
        }
    }

    @Override
    public Page<ProductResponseDto> getPagedProductsByFilter(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        Aggregation aggregation = getAggregation(pageable, storeType, productFilterRequestDto);

        AggregationResults<BaseEventProduct> aggregationResults = mongoTemplate.aggregate(
                aggregation, "event_product", BaseEventProduct.class
        );

        Criteria criteria = createCriteriaByFilter(storeType, productFilterRequestDto.getFilterList());
        return PageableExecutionUtils.getPage(
                aggregationResults.getMappedResults(),
                pageable,
                () -> mongoTemplate.count(new Query(criteria), BaseEventProduct.class)
        ).map(BaseEventProduct::convertToDto);
    }
}
