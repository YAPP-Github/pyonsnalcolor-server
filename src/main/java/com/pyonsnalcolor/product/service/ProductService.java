package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final EventProductRepository eventProductRepository;
    private final PbProductRepository pbProductRepository;

    public Page<ProductResponseDto> searchProduct(int pageNumber, int pageSize, String keyword) {
        String searchKeyword = removeSpecialCharacters(keyword);

        List<BaseEventProduct> eventProducts = eventProductRepository.findByNameContaining(searchKeyword);
        List<BasePbProduct> pbProducts = pbProductRepository.findByNameContaining(searchKeyword);

        Sort sort = Sort.by("updatedTime").descending().and(Sort.by("id"));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        List<BaseProduct> searchProducts = getProductsExceptDuplicate(eventProducts, pbProducts);
        List<ProductResponseDto> productResponseDtos = convertToProductResponseDto(searchProducts);

        return convertToPage(productResponseDtos, pageRequest);
    }

    private String removeSpecialCharacters(String searchKeyword) {
        String refinedKeyword = searchKeyword.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");
        return refinedKeyword;
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

    private Page<ProductResponseDto> convertToPage(List<ProductResponseDto> dtos, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), dtos.size());

        return new PageImpl<>(dtos.subList(start, end), pageRequest, dtos.size());
    }
}