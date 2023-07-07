package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.service.BatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CuPbBatchServiceTest {

    @Autowired
    @Qualifier("CuPb")
    private BatchService batchService;

    @Test
    @DisplayName("CU PB 상품 전체 플로우 실행")
    void integrationTest() {
        batchService.execute();
    }
}