package com.pyonsnalcolor.batch.service.gs25;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.batch.client.GS25Client;
import com.pyonsnalcolor.batch.client.GS25EventRequestBody;
import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.EventType;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.EventProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pyonsnalcolor.batch.service.gs25.GS25Constant.GS_MAIN_PAGE_URL;


@Service("GS25Event")
@Slf4j
public class GS25EventBatchService extends EventBatchService {
    private GS25Client gs25Client;
    private ObjectMapper objectMapper;

    private static final String NOT_EXIST = "NONE";

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
        String price = Double.toString((Double) productMap.get("price"));
        String eventType = (String) ((Map) productMap.get("eventTypeSp")).get("code");
        String giftImage = (String) productMap.get("giftAttFileNm");

        BaseEventProduct baseEventProduct = BaseEventProduct.builder()
                .originPrice(NOT_EXIST)
                .storeType(StoreType.GS25.getName())
                .updatedTime(LocalDateTime.now())
                .eventType(EventType.getEventTypeWithValue(eventType))
                .id(0L)
                .giftImage(giftImage)
                .image(image)
                .price(price)
                .name(name)
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

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get expired gs25 event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getNewProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get new event gs25 products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseEventProduct> gs25Products) {
        System.out.println("send event gs25 products alarms");
    }
}
