package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private PbProductRepository pbProductRepository;
    @Autowired
    private EventProductRepository eventProductRepository;

    @Test
    @DisplayName("검색 시, 해당 키워드가 포함된 제품이 조회됩니다.")
    void testSearchProduct() {
        // given
        BasePbProduct pbProduct = BasePbProduct.builder().name("비타 500").build();
        pbProductRepository.save(pbProduct);
        BaseEventProduct eventProduct = BaseEventProduct.builder().name("비타민 젤리").build();
        eventProductRepository.save(eventProduct);
        String keyword = "비타";

        // when
        Page<ProductResponseDto> products = productService.searchProduct(0, 2, keyword);
        ProductResponseDto responseDto1 = products.get().findFirst().get();
        ProductResponseDto responseDto2 = products.get().findFirst().get();

        // then
        assertAll(
                () -> assertThat(responseDto1.getName().contains(keyword)),
                () -> assertThat(responseDto2.getName().contains(keyword))
        );
    }

    @Test
    @DisplayName("검색 키워드에서 특수문자는 제외되고 조회합니다.")
    void testSearchProduct2() {
        // given
        BasePbProduct pbProduct = BasePbProduct.builder().name("연세 우유빵").build();
        pbProductRepository.save(pbProduct);
        String keyword = "연세)@#$";

        // when
        Page<ProductResponseDto> products = productService.searchProduct(0, 5, keyword);
        ProductResponseDto responseDto = products.get().findFirst().get();

        // then
        Assertions.assertThat(responseDto.getName().contains(keyword));
    }
}