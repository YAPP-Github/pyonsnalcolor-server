package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.*;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.Filter;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchProduct {
    private final EventProductRepository eventProductRepository;
    private final PbProductRepository pbProductRepository;

    public Page<ProductResponseDto> searchProduct(int pageNumber, int pageSize, String searchKeyword) {
        List<BaseEventProduct> eventProducts = eventProductRepository.findByNameContaining(searchKeyword);
        List<BasePbProduct> pbProducts = pbProductRepository.findByNameContaining(searchKeyword);

        Sort sort = Sort.by("updatedTime").descending().and(Sort.by("id"));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        List<BaseProduct> searchProducts = getProductsExceptDuplicate(eventProducts, pbProducts);
        List<ProductResponseDto> productResponseDtos = convertToProductResponseDto(searchProducts);

        return convertToPage(productResponseDtos, pageRequest);
    }

    private List<BaseProduct> getProductsExceptDuplicate(
            List<BaseEventProduct> eventProducts,
            List<BasePbProduct> pbProducts
    ) {
        List<BaseProduct> products = eventProducts.stream()
                .filter(event -> pbProducts.stream()
                        .noneMatch(pb -> event.getName().equals(pb.getName())))
                .collect(Collectors.toList());
        products.addAll(pbProducts);
        return products;
    }

    private List<ProductResponseDto> convertToProductResponseDto(List<BaseProduct> searchProducts) {
        List<ProductResponseDto> responseDtos = searchProducts.stream()
                .map(BaseProduct::convertToDto)
                .collect(Collectors.toList());

        return responseDtos;
    }

    private Page<ProductResponseDto> convertToPage(List<ProductResponseDto> list, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());

        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
    }

    public CurationProductsResponseDto getCurationProducts() {
        List<Curation> curations = Arrays.stream(Curation.values()).collect(Collectors.toUnmodifiableList());

        List<CurationProductResponseDto> curationProductResponseDtos = curations.stream()
                .map(curation -> {
                    List<PbProductResponseDto> products = pbProductRepository.findByCuration(curation)
                            .stream()
                            .map(BasePbProduct::convertToDto)
                            .collect(Collectors.toUnmodifiableList());
                    return CurationProductResponseDto.builder()
                            .title(curation.getKorean())
                            .subTitle(curation.getDescription())
                            .products(products)
                            .build();
                }).collect(Collectors.toUnmodifiableList());

        return new CurationProductsResponseDto(curationProductResponseDtos);
    }
}