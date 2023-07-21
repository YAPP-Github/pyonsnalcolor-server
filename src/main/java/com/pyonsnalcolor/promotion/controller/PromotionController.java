package com.pyonsnalcolor.promotion.controller;

import com.pyonsnalcolor.promotion.dto.PromotionResponseDto;
import com.pyonsnalcolor.promotion.service.PromotionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "프로모션 페이지 api")
@RestController
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @GetMapping("/promotions")
    public List<PromotionResponseDto> getPromotions(@RequestParam("storeType") String storeType) {
        return promotionService.getPromotions(storeType);
    }
}
