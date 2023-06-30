package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import org.springframework.stereotype.Service;

@Service
public class EventProductService extends ProductService<BaseEventProduct> {
    public EventProductService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }
}
