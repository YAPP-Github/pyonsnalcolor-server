package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.springframework.stereotype.Service;

@Service
public class PbProductService extends ProductService<BasePbProduct>{
    public PbProductService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
