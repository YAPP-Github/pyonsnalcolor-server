package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Emart24PromotionServiceTest {
    @Autowired
    @Qualifier("Emart24Promotion")
    private PromotionBatchService emart24PromotionService;

    @Test
    public void 통합_테스트() {
        emart24PromotionService.execute();
    }
}
