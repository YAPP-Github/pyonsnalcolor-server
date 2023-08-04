package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("Emart24Promotion")
@Slf4j
public class Emart24PromotionService extends PromotionBatchService {
    private static final String EMART_BASE_URL = "http://www.emart24.co.kr";
    private static final String EMART_PROMOTION_URL = EMART_BASE_URL + "/event/ing";

    @Autowired
    public Emart24PromotionService(PromotionRepository promotionRepository) {
        super(promotionRepository);
    }

    @Override
    public List<Promotion> getNewPromotions() {
        try {
            return getPromotions();
        } catch (Exception e) {
            //TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllProducts", e);
        }
        //TODO : 수정 필요
        return Collections.emptyList();
    }

    private List<Promotion> getPromotions() throws IOException {
        List<Promotion> promotions = new ArrayList<>();

        Document document = Jsoup.connect(EMART_PROMOTION_URL).get();
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
        try {
            String url = EMART_BASE_URL + imagePath;
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByClass("contentWrap").get(0);
            String image = element.getElementsByTag("img").get(0).attr("src");
            return image;
        } catch (Exception e) {
        }
        return null;
    }
}
