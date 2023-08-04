package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pyonsnalcolor.exception.model.CommonErrorCode.INVALID_FILTER_CODE;

@Slf4j
@Service
public class EventProductService extends ProductService{

    public EventProductService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    public Page<ProductResponseDto> getFilteredProducts(
            int pageNumber, int pageSize, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        List<Integer> filterList = productFilterRequestDto.getFilterList();
        validateProductFilterCodes(filterList);

        Comparator comparator = Sorted.getCategoryFilteredComparator(filterList);
        List<BaseEventProduct> eventProducts = getProductsListByFilter(storeType, filterList, BaseEventProduct.class);
        eventProducts.sort(comparator);

        List<ProductResponseDto> responseDtos = convertToResponseDtoList(eventProducts);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return convertToPage(responseDtos, pageRequest);
    }

    @Override
    protected void validateProductFilterCodes(List<Integer> filterList) {
        List<Integer> codes = Stream.of(
                Filter.getCodes(Category.class),
                Filter.getCodes(Sorted.class),
                Filter.getCodes(EventType.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!codes.containsAll(filterList)) {
            throw new PyonsnalcolorProductException(INVALID_FILTER_CODE);
        }
    }
}