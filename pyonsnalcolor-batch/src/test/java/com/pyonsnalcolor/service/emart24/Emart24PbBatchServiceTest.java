package com.pyonsnalcolor.service.emart24;

import com.pyonsnalcolor.service.BatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Emart24PbBatchServiceTest {
    @Autowired
    @Qualifier("Emart24Pb")
    private BatchService batchService;

    @Test
    @DisplayName("이마트24 Pb 상품 전체 플로우 실행")
    public void integrationTest() {
        batchService.execute();
    }
}
