package com.pyonsnalcolor.batch.service.gs25;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pyonsnalcolor.batch.client.GS25Client;
import com.pyonsnalcolor.batch.client.GS25EventRequestBody;
import com.pyonsnalcolor.batch.client.GS25PbRequestBody;
import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.model.EventType;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
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


@Service("GS25Pb")
@Slf4j
public class GS25PbBatchService extends PbBatchService {
    private GS25Client gs25Client;
    private ObjectMapper objectMapper;

    private static final String NOT_EXIST = "NONE";

    @Autowired
    public GS25PbBatchService(PbProductRepository pbProductRepository,
                              GS25Client gs25Client,
                              ObjectMapper objectMapper) {
        super(pbProductRepository);
        this.gs25Client = gs25Client;
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        List<BasePbProduct> results = new ArrayList<>();
        try {
            Map<String, String> accessInfo = getAccessInfo();

            String csrfToken = accessInfo.get("csrfToken");
            String sessionId = accessInfo.get("sessionId");
            String cookie = String.format("%s=%s;", "JSESSIONID", sessionId);

            GS25PbRequestBody requestBody = new GS25PbRequestBody();
            int numberOfPages = 0;

            do {
                Object pbProducts = gs25Client.getPbProducts(cookie, csrfToken, requestBody);
                Map<String, Object> paginationMap = parsePaginationData(pbProducts);

                requestBody.updateNextPage();
                numberOfPages = (Integer) paginationMap.get("numberOfPages");

                results.addAll(parseProductsData(pbProducts));
            } while (requestBody.getPageNum() <= numberOfPages);
        } catch (Exception e) {
            //TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllProducts", e);
        }

        System.out.println(results.size());
        for (BasePbProduct result : results) {
            System.out.println("result = " + result);
        }

        return null;
    }

    @Override
    protected List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        return null;
    }

    private List<BasePbProduct> parseProductsData(Object data) throws JsonProcessingException {
        List<BasePbProduct> basePbProducts = new ArrayList<>();
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        Object results = dataMap.get("SubPageListData");

        List productList = (List) results;
        for (Object product : productList) {
            BasePbProduct basePbProduct = convertToBasePbProduct(product);
            basePbProducts.add(basePbProduct);
        }
        return basePbProducts;
    }

    private BasePbProduct convertToBasePbProduct(Object product) {
        Map<String, Object> productMap = objectMapper.convertValue(product, Map.class);
        String image = (String) productMap.get("attFileNm");
        String name = (String) productMap.get("goodsNm");
        String price = Double.toString((Double) productMap.get("price"));

        BasePbProduct basePbProduct = BasePbProduct.builder()
                .id(0L)
                .name(name)
                .price(price)
                .storeType(StoreType.GS25.getName())
                .updatedTime(LocalDateTime.now())
                .image(image)
                .build();

        return basePbProduct;
    }

    private Map<String, Object> parsePaginationData(Object data) throws JsonProcessingException {
        Map<String, Object> dataMap = objectMapper.readValue((String) data, Map.class);
        Object pagination = dataMap.get("SubPageListPagination");
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

    @Override
    protected void sendAlarms(List<BasePbProduct> gs25Products) {
        System.out.println("send gs25 pb products alarms");
    }
}
