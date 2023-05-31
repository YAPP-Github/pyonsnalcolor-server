package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CuPb")
@Slf4j
public class CuPbBatchService extends PbBatchService {

    @Autowired
    public CuPbBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getNewProducts() {
        /**
         * TODO : 크롤링하여 새 상품 데이터들을 반환하는 기능을 구현하시면 됩니다.
         */
        System.out.println("get new Cu pb products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> CuProducts) {
        System.out.println("send Cu pb products alarms");
    }
}
