package com.pyonsnalcolor.batch.service.gs25;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.batch.client.GS25Client;
import com.pyonsnalcolor.batch.client.GS25EventRequestBody;
import com.pyonsnalcolor.batch.service.EventBatchService;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.Recommend;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pyonsnalcolor.batch.service.gs25.GS25Constant.GS_MAIN_PAGE_URL;
import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("GS25Event")
@Slf4j
public class GS25EventBatchService extends EventBatchService {
    private GS25Client gs25Client;
    private ObjectMapper objectMapper;

    private static final String NOT_EXIST = null;

    @Autowired
    public GS25EventBatchService(EventProductRepository eventProductRepository,
                                 GS25Client gs25Client,
                                 ObjectMapper objectMapper) {
        super(eventProductRepository);
        this.gs25Client = gs25Client;
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        List<BaseEventProduct> results = new ArrayList<>();
        try {
            Map<String, String> accessInfo = getAccessInfo();

            String csrfToken = accessInfo.get("csrfToken");
            String sessionId = accessInfo.get("sessionId");
            String cookie = String.format("%s=%s;", "JSESSIONID", sessionId);

            int numberOfPages = 0;
            GS25EventRequestBody requestBody = new GS25EventRequestBody();

            do {
                Object eventProducts = gs25Client.getEventProducts(cookie, csrfToken, requestBody);
                Map<String, Integer> pagination = parsePaginationData(eventProducts);

                requestBody.updateNextPage();
                numberOfPages = pagination.get("numberOfPages");

                results.addAll(parseProductsData(eventProducts));
            } while (requestBody.getPageNum() <= numberOfPages);
        } catch(Exception e) {
            //TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllProducts", e);
        }

        return results;
    }

    private Map<String, Integer> parsePaginationData(Object data) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        Object pagination = dataMap.get("pagination");
        Map<String, Integer> paginationMap = objectMapper.convertValue(pagination, Map.class);

        return paginationMap;
    }

    private List<BaseEventProduct> parseProductsData(Object data) throws JsonProcessingException {
        List<BaseEventProduct> baseEventProducts = new ArrayList<>();
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        Object results = dataMap.get("results");

        List productList = (List) results;
        for (Object product : productList) {
            BaseEventProduct baseEventProduct = convertToBaseEventProduct(product);
            baseEventProducts.add(baseEventProduct);
        }
        return baseEventProducts;
    }

    private BaseEventProduct convertToBaseEventProduct(Object product) {
        Map<String, Object> productMap = objectMapper.convertValue(product, Map.class);
        String image = (String) productMap.get("attFileNm");
        String name = (String) productMap.get("goodsNm");
        String price = Double.toString((Double) productMap.get("price")).split("\\.")[0];
        int priceInt = Integer.parseInt(price);
        String formattedPrice = NumberFormat.getInstance().format(priceInt);
        String eventType = (String) ((Map) productMap.get("eventTypeSp")).get("code");
        String giftImage = (String) productMap.get("giftAttFileNm");
        String giftTitle = (String) productMap.get("giftGoodsNm");
        Double giftPriceDouble = (Double) productMap.get("giftPrice");
        String formattedGiftPrice = null;
        if (giftPriceDouble != null) {
            String giftPrice =  Double.toString(giftPriceDouble).split("\\.")[0];
            int giftPriceInt = Integer.parseInt(giftPrice);
            formattedGiftPrice = NumberFormat.getInstance().format(giftPriceInt);
        }

        Category category = Category.matchCategoryByProductName(name);
        Recommend recommend = Recommend.matchRecommendByProductName(name);

        BaseEventProduct baseEventProduct = BaseEventProduct.builder()
                .originPrice(NOT_EXIST)
                .storeType(StoreType.GS25)
                .updatedTime(LocalDateTime.now())
                .eventType(EventType.valueOf(eventType))
                .id(generateId())
                .giftImage(giftImage)
                .giftTitle(giftTitle)
                .giftPrice(formattedGiftPrice)
                .image(image)
                .price(formattedPrice)
                .name(name)
                .category(category)
                .recommend(recommend)
                .build();

        return baseEventProduct;
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
