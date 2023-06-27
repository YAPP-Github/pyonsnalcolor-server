package com.pyonsnalcolor.service;

import com.pyonsnalcolor.model.BaseEventProduct;
import com.pyonsnalcolor.repository.EventProductRepository;
import org.springframework.stereotype.Service;

@Service
public class EventProductService extends ProductService<BaseEventProduct> {
    public EventProductService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }
}
