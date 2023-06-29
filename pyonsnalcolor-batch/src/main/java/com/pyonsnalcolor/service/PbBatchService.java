package com.pyonsnalcolor.service;

import com.pyonsnalcolor.model.BasePbProduct;
import com.pyonsnalcolor.repository.PbProductRepository;

public abstract class PbBatchService extends BasicBatchServiceTemplate<BasePbProduct> {
    public PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
