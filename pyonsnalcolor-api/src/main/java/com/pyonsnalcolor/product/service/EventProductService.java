package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventProductService extends ProductService<BaseEventProduct> {

    public EventProductService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    public Page<EventProductResponseDto> getProductsByPaging(int pageNumber, int pageSize, String storeType, String sorted) {

        Sort sort = Sort.by("updatedTime").descending().and(Sort.by("id"));
        String storeTypeUppercase = storeType.toUpperCase();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        if (storeTypeUppercase.equals("ALL")) {
            Page<BaseEventProduct> products = basicProductRepository.findAll(pageRequest);
            return convertToEventProductResponseDto(products);
        }
        StoreType storeTypeEnum = StoreType.valueOf(storeTypeUppercase);
        Page<BaseEventProduct> products = basicProductRepository.findByStoreType(storeTypeEnum, pageRequest);
        return convertToEventProductResponseDto(products);
    }

    private Page<EventProductResponseDto> convertToEventProductResponseDto(Page<BaseEventProduct> products) {
        List<EventProductResponseDto> productResponseDtos = products.getContent().stream()
                .map(BaseEventProduct::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtos, products.getPageable(), products.getTotalElements());
    }
}