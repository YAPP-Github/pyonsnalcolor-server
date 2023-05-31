package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CuEvent")
@Slf4j
public class CuEventBatchService extends EventBatchService {
    @Autowired
    public CuEventBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getAllProducts() {
        /**
         * TODO : 여기 크롤링한 모든 상품들을 반환하는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get expired Cu event products");
        return null;
    }

    @Override
    protected List<BaseProduct> getEventExpiredProducts(List<BaseProduct> allProducts) {
        /**
         * TODO : 전체 이벤트 데이터들에서 기간이 끝난 상품들을 골라내는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get expired Cu event products");
        return null;
    }

    @Override
    protected List<BaseProduct> getNewProducts(List<BaseProduct> allProducts) {
        /**
         * TODO : 전체 이벤트 데이터들에서 새롭게 등장한 상품들을 골라내는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get new event Cu products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> CuProducts) {
        System.out.println("send event Cu products alarms");
    }
}
