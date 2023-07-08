package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.BasicProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.NoSuchElementException;

@AllArgsConstructor
public class ProductService<T extends BaseProduct> {
    protected BasicProductRepository basicProductRepository;

    public Page<ProductResponseDto> getProductsWithPaging(int pageNumber, int pageSize, String storeType, String sorted) {
        Page<T> products;
        switch (storeType.toUpperCase()) {
            case "CU":
            case "GS25":
            case "EMART24":
            case "SEVEN_ELEVEN":
                products = basicProductRepository.findByStoreType(
                        Enum.valueOf(StoreType.class, storeType.toUpperCase()),
                        PageRequest.of(pageNumber, pageSize, Sort.by(sorted))
                );
            default:
                products = basicProductRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("updatedTime")));
        }
        products.getContent()
                .stream()
                .map( p ->
                        p.toDto()
                )

        List<Dto> dtoList = entityPage.getContent()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());

    }

    public ProductResponseDto convert(BaseProduct baseProduct) {
        if(baseProduct instanceof ) {

        } else if() {

        }
    }

    public ProductResponseDto getProduct(String id) throws Throwable {
        T product = (T) basicProductRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
