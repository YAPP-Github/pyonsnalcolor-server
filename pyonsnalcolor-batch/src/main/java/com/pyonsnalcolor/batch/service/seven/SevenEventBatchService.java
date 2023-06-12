package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.repository.EventProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SevenEvent")
@Slf4j
public class SevenEventBatchService extends EventBatchService {
    @Autowired
    public SevenEventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        /**
         * TODO : 여기 크롤링한 모든 상품들을 반환하는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get expired Seven event products");
        return null;
    }
}
