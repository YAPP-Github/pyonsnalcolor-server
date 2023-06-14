package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PbBatchService extends BasicBatchServiceTemplate<BasePbProduct> {
    public PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
