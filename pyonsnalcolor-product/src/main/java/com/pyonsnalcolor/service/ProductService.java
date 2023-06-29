package com.pyonsnalcolor.service;

import com.pyonsnalcolor.model.BaseProduct;
import com.pyonsnalcolor.model.StoreType;
import com.pyonsnalcolor.repository.BasicProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
public class ProductService<T extends BaseProduct> {
    protected BasicProductRepository basicProductRepository;

    public Page<T> getProductsWithPaging(int pageNumber, int pageSize, String storeType, String sorted) {
        switch (storeType.toUpperCase()) {
            case "CU":
            case "GS25":
            case "EMART24":
            case "SEVEN_ELEVEN":
                return basicProductRepository.findByStoreType(
                        Enum.valueOf(StoreType.class, storeType.toUpperCase()),
                        PageRequest.of(pageNumber, pageSize, Sort.by(sorted))
                );
            default:
                return basicProductRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("updatedTime")));
        }
    }
}
