package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SevenPromotionBatchServiceTest {

    @Autowired
    @Qualifier("SevenPromotion")
    PromotionBatchService promotionBatchService;

    @Test
    void integrationTest() {
        promotionBatchService.execute();
    }
}