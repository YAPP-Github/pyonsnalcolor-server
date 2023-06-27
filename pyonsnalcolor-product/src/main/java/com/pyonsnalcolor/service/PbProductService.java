package com.pyonsnalcolor.service;

import com.pyonsnalcolor.model.BasePbProduct;
import com.pyonsnalcolor.repository.PbProductRepository;
import org.springframework.stereotype.Service;

@Service
public class PbProductService extends ProductService<BasePbProduct>{
    public PbProductService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
