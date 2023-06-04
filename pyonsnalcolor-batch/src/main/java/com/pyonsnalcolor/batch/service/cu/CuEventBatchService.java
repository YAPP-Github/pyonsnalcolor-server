package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.EventType;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.EventProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("CuEvent")
@Slf4j
public class CuEventBatchService extends EventBatchService {

    private static final String CU_URL= "https://cu.bgfretail.com/event/plusAjax.do?";

    @Autowired
    public CuEventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        List<BaseEventProduct> results = new ArrayList<>();

        try {
            results.addAll(getProducts());
        } catch (Exception e) {
            log.error("해당 페이지에 접근하지 못합니다. {}", e.getMessage());
        }
        return results;
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        /**
         * TODO : 전체 이벤트 데이터들에서 기간이 끝난 상품들을 골라내는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get expired Cu event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getNewProducts(List<BaseEventProduct> allProducts) {
        /**
         * TODO : 전체 이벤트 데이터들에서 새롭게 등장한 상품들을 골라내는 기능을 구현해주시면 됩니다.
         */
        System.out.println("get new event Cu products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseEventProduct> CuProducts) {
        System.out.println("send event Cu products alarms");
    }

    private List<BaseEventProduct> getProducts() throws IOException {
        List<BaseEventProduct> products = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String CU_EVENT_URL_TMP = CU_URL+ "pageIndex=" + pageIndex;
            Document doc = Jsoup.connect(CU_EVENT_URL_TMP).timeout(0).get();
            Elements elements = doc.select("a.prod_item");

            if (elements.isEmpty()) {
                break;
            }

            List<BaseEventProduct> tmp = elements.stream()
                    .map(this::convertToBaseEventProduct)
                    .collect(Collectors.toList());
            products.addAll(tmp);
            pageIndex++;
        }
        return products;
    }

    private BaseEventProduct convertToBaseEventProduct(Element element) {
        String name = element.select("div.name").first().text();
        String image = element.select("img.prod_img").first().attr("src");
        String price = element.select("div.price > strong").first().text();
        String eventTypeTag = element.select("div.badge").first().text();
        EventType eventType = EventType.getEventTypeWithValue(eventTypeTag);
        
        return BaseEventProduct.builder()
                .name(name)
                .image(image)
                .price(price)
                .eventType(eventType)
                .storeType(StoreType.CU.getName())
                .updatedTime(LocalDateTime.now())
                .build();
    }
}