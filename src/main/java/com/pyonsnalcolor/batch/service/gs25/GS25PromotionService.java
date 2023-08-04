package com.pyonsnalcolor.batch.service.gs25;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.batch.client.GS25Client;
import com.pyonsnalcolor.batch.client.GS25PromotionRequestBody;
import com.pyonsnalcolor.batch.service.PromotionBatchService;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.batch.service.gs25.GS25Constant.GS_MAIN_PAGE_URL;
import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("GS25Promotion")
@Slf4j
public class GS25PromotionService extends PromotionBatchService {
    private GS25Client gs25Client;
    private ObjectMapper objectMapper;

    @Autowired
    public GS25PromotionService(PromotionRepository promotionRepository, GS25Client gs25Client, ObjectMapper objectMapper) {
        super(promotionRepository);
        this.gs25Client = gs25Client;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Promotion> getNewPromotions() {
        List<Promotion> results = new ArrayList<>();

        try {
            Map<String, String> accessInfo = getAccessInfo();

            String csrfToken = accessInfo.get("csrfToken");
            String sessionId = accessInfo.get("sessionId");
            String cookie = String.format("%s=%s;", "JSESSIONID", sessionId);

            GS25PromotionRequestBody requestBody = new GS25PromotionRequestBody();
            int numberOfPages = 0;

            do {
                Object promotions = gs25Client.getPromotions(cookie, csrfToken, requestBody);

                Map<String, Object> paginationMap = parsePaginationData(promotions);

                requestBody.updateNextPage();
                numberOfPages = (Integer) paginationMap.get("numberOfPages");

                results.addAll(parsePromotionsData(promotions));
            } while (requestBody.getPageNum() <= numberOfPages);
        } catch (Exception e) {
            // TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllPromotions", e);
        }
        return results;
    }


    private List<Promotion> parsePromotionsData(Object data) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        List<Object> results = (List) dataMap.get("results");

        List<Promotion> promotions = results.stream()
                .map(this::convertToPromotion)
                .map(this::validateAllFieldsNotNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return promotions;
    }

    private Promotion convertToPromotion(Object promotion) {
        Map<String, Object> promotionMap = objectMapper.convertValue(promotion, Map.class);
        String title = (String) promotionMap.get("title");
        String eventCode = (String) promotionMap.get("eventCode");
        String image = getImageUrl(eventCode);
        String thumbnailImage = (String) ((Map) promotionMap.get("landscapeThumbnail")).get("url");

        return Promotion.builder()
                .id(generateId())
                .storeType(StoreType.GS25)
                .image(image)
                .thumbnailImage(thumbnailImage)
                .title(title)
                .build();
    }

    private String getImageUrl(String eventCode) {
        try {
            final String eventUrlTemplate
                    = "http://gs25.gsretail.com/gscvs/ko/customer-engagement/event/detail/publishing?eventCode=%s";

            Document document = Jsoup.connect(String.format(eventUrlTemplate, eventCode)).get();
            Elements products = document.getElementsByClass("cnt");
            Elements img = products.get(0).getElementsByTag("img");

            return img.get(0).attr("src");
        } catch(Exception e) {}

        return null;
    }

    private Map<String, Object> parsePaginationData(Object data) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        Object pagination = dataMap.get("pagination");
        Map<String, Object> paginationMap = objectMapper.convertValue(pagination, Map.class);

        return paginationMap;
    }

    private Map<String, String> getAccessInfo() throws IOException {
        Connection connect = Jsoup.connect(GS_MAIN_PAGE_URL);
        String csrfToken = getCsrfToken(connect);
        String sessionId = getSessionId(connect);

        return Map.of(
                "csrfToken", csrfToken,
                "sessionId", sessionId
        );
    }

    private String getCsrfToken(Connection connection) throws IOException {
        Document document = connection.get();
        Elements csrfElement = document.getElementsByAttributeValue("name", "CSRFToken");
        String csrfToken = csrfElement.attr("value");

        return csrfToken;
    }

    private String getSessionId(Connection connection) {
        Connection.Response response = connection.response();
        String jsessionid = response.cookie("JSESSIONID");

        return jsessionid;
    }
}