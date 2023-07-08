package com.pyonsnalcolor.promotion.service;

import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.dto.PromotionResponseDto;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    public List<PromotionResponseDto> getPromotions(StoreType storeTYpe) {
        List<Promotion> promotions = promotionRepository.findByStoreType(storeTYpe);

        return promotions.stream()
                .map(p -> p.convertToDto())
                .collect(Collectors.toList());
    }
}
