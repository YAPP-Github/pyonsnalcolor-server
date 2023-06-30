package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.service.BatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GS25EventBatchServiceTest {
    @Autowired
    @Qualifier("GS25Event")
    private BatchService batchService;

    @Test
    @DisplayName("GS24 이벤트 상품 전체 플로우 실행")
    public void integrationTest() {
        batchService.execute();
    }
}
