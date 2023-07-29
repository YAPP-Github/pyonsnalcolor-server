package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.exception.model.CommonErrorCode;
import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PbProductService extends ProductService {

    public PbProductService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    // TODO: v2 필터 조회 api 연동 후 삭제
    public Page<PbProductResponseDto> getProductsWithPaging(int pageNumber, int pageSize, String storeType, String filterList) {

        Sort sort = Sort.by("updatedTime").descending().and(Sort.by("id"));
        String storeTypeUppercase = storeType.toUpperCase();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        if (storeTypeUppercase.equals("ALL")) {
            Page<BasePbProduct> products = basicProductRepository.findAll(pageRequest);
            return convertToPbProductResponseDto(products);
        }
        StoreType storeTypeEnum = StoreType.valueOf(storeTypeUppercase);
        Page<BasePbProduct> products = basicProductRepository.findByStoreType(storeTypeEnum, pageRequest);

        return convertToPbProductResponseDto(products);
    }

    // TODO: v2 필터 조회 api 연동 후 삭제
    private Page<PbProductResponseDto> convertToPbProductResponseDto(Page<BasePbProduct> products) {
        List<PbProductResponseDto> productResponseDtos = products.getContent().stream()
                .map(BasePbProduct::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtos, products.getPageable(), products.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getFilteredProducts(
            int pageNumber, int pageSize, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        List<Integer> filterList = productFilterRequestDto.getFilterList();
        validateProductFilterCodes(filterList);

        Comparator comparator = Sorted.getCategoryFilteredComparator(filterList);
        List<BasePbProduct> responseDtos = getProductsListByFilter(storeType, filterList, BasePbProduct.class);
        responseDtos.sort(comparator);

        List<ProductResponseDto> result = convertToResponseDtoList(responseDtos);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return convertToPage(result, pageRequest);
    }

    @Override
    protected void validateProductFilterCodes(List<Integer> filterList) {
        List<Integer> codes = Stream.of(
                Filter.getCodes(Category.class),
                Filter.getCodes(Sorted.class),
                Filter.getCodes(Recommend.class),
                Filter.getCodes(EventType.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!codes.containsAll(filterList)) {
            throw new PyonsnalcolorProductException(CommonErrorCode.INVALID_FILTER_CODE);
        }
    }
}