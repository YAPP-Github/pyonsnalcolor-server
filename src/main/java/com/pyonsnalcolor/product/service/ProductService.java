package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.BasicProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class ProductService {
    protected final BasicProductRepository basicProductRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ProductResponseDto getProduct(String id) {
        Optional<BaseProduct> baseProduct = basicProductRepository.findById(id);
        baseProduct.orElseThrow(NoSuchElementException::new);
        return baseProduct.get().convertToDto();
    }

    protected List<ProductResponseDto> convertToResponseDtoList(List<? extends BaseProduct> products) {
        return products.stream()
                .map(BaseProduct::convertToDto)
                .collect(Collectors.toList());
    }

    protected abstract Page<ProductResponseDto> getFilteredProducts(
            int pageNumber, int pageSize, String storeType, ProductFilterRequestDto productFilterRequestDto);

    protected abstract void validateProductFilterCodes(List<Integer> filterList);

    protected Page<ProductResponseDto> convertToPage(List<ProductResponseDto> list, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
    }

    protected <T extends BaseProduct> List<T> getProductsListByFilter(
            String storeType, List<Integer> filterList, Class<T> productClass
    ) {
        Query query = createQueryByFilter(storeType, filterList);
        return mongoTemplate.find(query, productClass);
    }

    private Query createQueryByFilter(String storeType, List<Integer> filterList) {
        List<Recommend> recommends = Filter.findEnumByFilterList(Recommend.class, filterList);
        List<Category> categories = Filter.findEnumByFilterList(Category.class, filterList);
        List<EventType> eventTypes = Filter.findEnumByFilterList(EventType.class, filterList);

        Criteria criteria = createFilterCriteria(recommends, categories, eventTypes, storeType);
        return new Query(criteria);
    }

    private Criteria createFilterCriteria(List<Recommend> recommends, List<Category> categories,
                                          List<EventType> eventTypes, String storeType) {
        Criteria criteria = new Criteria();
        criteria = Filter.getCriteria(recommends, "recommend", criteria);
        criteria = Filter.getCriteria(categories, "category", criteria);
        criteria = Filter.getCriteria(eventTypes, "eventType", criteria);
        criteria = StoreType.getCriteria(storeType, criteria);

        return criteria;
    }
}