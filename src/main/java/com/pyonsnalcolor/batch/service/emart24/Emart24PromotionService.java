package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("Emart24Promotion")
@Slf4j
public class Emart24PromotionService extends PromotionBatchService {
    private static final String EMART_BASE_URL = "http://www.emart24.co.kr";
    private static final String EMART_PROMOTION_URL = EMART_BASE_URL + "/event/ing";
    private static final int TIMEOUT = 20000;

    @Autowired
    public Emart24PromotionService(PromotionRepository promotionRepository) {
        super(promotionRepository);
    }

    @Override
    public List<Promotion> getNewPromotions() {
        return BatchExceptionUtil.handleException(this::getPromotions);
    }

    private List<Promotion> getPromotions() {
        List<Promotion> promotions = new ArrayList<>();

        Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(EMART_PROMOTION_URL, TIMEOUT);
        Elements elements = document.getElementsByClass("eventWrap");

        List<Promotion> pagedPromotions = elements.stream()
                .map(this::convertToPromotion)
                .map(this::validateAllFieldsNotNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        promotions.addAll(pagedPromotions);
        return promotions;
    }

    private Promotion convertToPromotion(Element element) {
        String thumbnailImage = element.getElementsByTag("img").get(0).attr("src");
        String title = element.getElementsByTag("p").get(0).text().split(" ", 4)[3];
        String imagePath = element.attr("href");
        String image = getImageUrl(imagePath);

        return Promotion.builder()
                .id(generateId())
                .image(image)
                .thumbnailImage(thumbnailImage)
                .title(title)
                .storeType(StoreType.EMART24)
                .build();
    }

    public String getImageUrl(String imagePath) {
        String url = EMART_BASE_URL + imagePath;
        Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(url, TIMEOUT);
        Element element = document.getElementsByClass("contentWrap").get(0);
        String image = element.getElementsByTag("img").get(0).attr("src");
        return image;
    }
}