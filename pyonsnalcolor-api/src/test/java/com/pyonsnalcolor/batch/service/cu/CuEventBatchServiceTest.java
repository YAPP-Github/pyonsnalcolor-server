package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.service.BatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CuEventBatchServiceTest {

    @Autowired
    @Qualifier("CuEvent")
    private BatchService batchService;

    @Test
    @DisplayName("CU 이벤트 상품 전체 플로우 실행")
    void integrationTest() {
        batchService.execute();
    }
}