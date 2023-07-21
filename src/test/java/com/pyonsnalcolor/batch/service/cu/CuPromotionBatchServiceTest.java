package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CuPromotionBatchServiceTest {

    @Autowired
    @Qualifier("CuPromotion")
    PromotionBatchService promotionBatchService;

    @Test
    void integrationTest() {
        promotionBatchService.execute();
    }
}