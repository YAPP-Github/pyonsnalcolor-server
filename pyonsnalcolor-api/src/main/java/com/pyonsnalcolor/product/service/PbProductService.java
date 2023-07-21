package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PbProductService {

    private final PbProductRepository pbProductRepository;
    private final EventProductRepository eventProductRepository;

    public Page<PbProductResponseDto> getProductsWithPaging(int pageNumber, int pageSize, String storeType, String filterList) {

        Sort sort = Sort.by("updatedTime").descending().and(Sort.by("id"));
        String storeTypeUppercase = storeType.toUpperCase();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        if (storeTypeUppercase.equals("ALL")) {
            Page<BasePbProduct> products = pbProductRepository.findAll(pageRequest);
            return convertToPbProductResponseDto(products);
        }
        StoreType storeTypeEnum = StoreType.valueOf(storeTypeUppercase);
        Page<BasePbProduct> products = pbProductRepository.findByStoreType(storeTypeEnum, pageRequest);
        return convertToPbProductResponseDto(products);
    }
    private Page<PbProductResponseDto> convertToPbProductResponseDto(Page<BasePbProduct> products) {
        List<PbProductResponseDto> productResponseDtos = products.getContent().stream()
                .map(BasePbProduct::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtos, products.getPageable(), products.getTotalElements());
    }

    public ProductResponseDto getProduct(String id) {
        ProductResponseDto product = pbProductRepository.findById(id)
                .map(BasePbProduct::convertToDto)
                .orElseThrow(NoSuchElementException::new);
        setEventType(product);

        return product;
    }

    public void setEventType(ProductResponseDto productResponseDto) {
        Optional<BaseEventProduct> eventProduct = eventProductRepository.findByName(productResponseDto.getName());
        if(eventProduct.isPresent()) {
            productResponseDto.setEventType(eventProduct.get().getEventType());
        }
    }
}