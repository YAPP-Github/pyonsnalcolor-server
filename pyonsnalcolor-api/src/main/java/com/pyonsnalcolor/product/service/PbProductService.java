package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PbProductService extends ProductService<BasePbProduct>{
    @Autowired
    private EventProductRepository eventProductRepository;

    public PbProductService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    public Page<ProductResponseDto> getProductsWithPaging(int pageNumber, int pageSize, String storeType, String sorted) {
        Page<ProductResponseDto> productResponseDtos = super.getProductsWithPaging(pageNumber, pageSize, storeType, sorted);
        productResponseDtos.getContent().forEach(
                p -> setEventType(p)
        );

        return productResponseDtos;
    }

    @Override
    public ProductResponseDto getProduct(String id) throws Throwable {
        ProductResponseDto product = super.getProduct(id);
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
