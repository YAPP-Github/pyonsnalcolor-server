package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.model.BasePbProduct;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.springframework.stereotype.Service;

@Service
public class PbProductService extends ProductService<BasePbProduct>{
    public PbProductService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
