package com.pyonsnalcolor.service.emart24;

import com.pyonsnalcolor.model.BaseEventProduct;
import com.pyonsnalcolor.model.EventType;
import com.pyonsnalcolor.model.StoreType;
import com.pyonsnalcolor.repository.EventProductRepository;
import com.pyonsnalcolor.service.BatchService;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static com.pyonsnalcolor.model.UUIDGenerator.generateId;

@SpringBootTest
class Emart24EventBatchServiceTest {
    @Autowired
    @Qualifier("Emart24Event")
    private BatchService batchService;
    @Autowired
    private EventProductRepository eventProductRepository;

    @Test
    @Ignore
    @DisplayName("이마트24 이벤트 상품 전체 플로우 실행")
    public void integrationTest() {
        batchService.execute();
    }

    @Test
    @Ignore
    @DisplayName("이벤트 기간이 끝난 상품들을 삭제한다")
    public void deleteExpiredEventProductsTest() {
        //given
        String testId = generateId();
        BaseEventProduct expiredProduct = BaseEventProduct.builder()
                .name("testName")
                .image("testImage")
                .price("testPrice")
                .eventType(EventType.ONE_TO_ONE)
                .storeType(StoreType.EMART24)
                .id(testId)
                .updatedTime(LocalDateTime.now())
                .build();
        eventProductRepository.save(expiredProduct);

        //when
        batchService.execute();

        //then
        Assertions.assertThat(eventProductRepository.findById(testId).isEmpty()).isTrue();
    }
}
