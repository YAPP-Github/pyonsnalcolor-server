package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GS25PromotionServiceTest {
    @Autowired
    @Qualifier("GS25Promotion")
    private PromotionBatchService gs25PromotionService;

    @Test
    public void 통합_테스트() {
        gs25PromotionService.execute();
    }
}
