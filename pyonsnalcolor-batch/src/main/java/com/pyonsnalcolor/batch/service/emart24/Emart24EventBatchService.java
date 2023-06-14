package com.pyonsnalcolor.batch.service.emart24;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pyonsnalcolor.batch.model.UUIDGenerator.generateId;

@Service("Emart24Event")
@Slf4j
public class Emart24EventBatchService extends EventBatchService {
    private static final String EMART_EVENT_URL_TEMPLATE = "http://www.emart24.co.kr/goods/event?search=&page=%s&category_seq=&align=";
    private static final String NOT_EXIST = "NONE";

    @Autowired
    public Emart24EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        try {
            List<BaseEventProduct> results = new ArrayList<>();
            Elements productElements;
            int curPage = 1;

            do {
                String url = String.format(EMART_EVENT_URL_TEMPLATE, curPage);
                Document document = Jsoup.connect(url).get();
                productElements = document.getElementsByClass("itemWrap");

                results.addAll(parseProductsData(productElements));
                curPage++;
            } while (productElements.size() > 0);

            for(BaseEventProduct b: results) {
                System.out.println("b = " + b);
            }

            return results;
        } catch (Exception e) {
            //TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllProducts", e);
        }
        return Collections.emptyList();
    }

    private List<BaseEventProduct> parseProductsData(Elements productElements) {
        List<BaseEventProduct> results = new ArrayList<>();

        for (Element productElement : productElements) {
            String giftImage = parseGiftImage(productElement);
            EventType eventType = parseEventType(productElement);
            String name = parseName(productElement);
            String price = convertToNum(productElement.getElementsByClass("price").get(0).text());
            String originPrice = parseOriginPrice(productElement);
            String image = productElement.getElementsByTag("img").attr("src");

            results.add(convertToBasePbProduct(image, name, price, originPrice, giftImage, eventType));
        }
        return results;
    }

    private String convertToNum(String price) {
        String priceInfo = price.split(" ")[0];
        String result = priceInfo.replace(",", "");

        return result;
    }

    private String parseGiftImage(Element productElement) {
        Elements giftElement = productElement.select("div.dumgift");
        String giftImage = NOT_EXIST;
        if(giftElement.size() > 0) {
            giftImage = giftElement.get(0).getElementsByTag("img").attr("src");
        }

        return giftImage;
    }

    private String parseOriginPrice(Element productElement) {
        String originPrice = NOT_EXIST;
        Elements originPriceElement = productElement.getElementsByClass("priceOff");
        if(originPriceElement.size() > 0) {
            originPrice = originPriceElement.get(0).text();
        }

        return originPrice;
    }

    private String parseName(Element productElement) {
        Elements itemTitle = productElement.getElementsByClass("itemtitle");
        Element element = itemTitle.get(0);
        String name = element.getElementsByTag("a").get(0).text();

        return name;
    }

    private EventType parseEventType(Element productElement) {
        Elements eventTypeElement = productElement.select("span[class$=floatR]");
        String eventName = eventTypeElement.attr("class");
        eventName = eventName.split(" ")[0];
        EventType eventType = getEventType(eventName);

        return eventType;
    }

    private EventType getEventType(String eventName) {
        switch(eventName) {
            case "dum":
                return EventType.GIFT;
            case "sale":
                return EventType.DISCOUNT;
            case "tripl":
                return EventType.THREE_TO_ONE;
            case "twopl":
                return EventType.TWO_TO_ONE;
            case "onepl":
                return EventType.ONE_TO_ONE;
            default:
                throw new IllegalArgumentException(eventName + "is not support");
        }
    }

    private BaseEventProduct convertToBasePbProduct(String image, String name, String price, String originPrice, String giftImage, EventType eventType) {
        BaseEventProduct baseEventProduct = BaseEventProduct.builder()
                .originPrice(originPrice) //변경
                .storeType(StoreType.EMART24)
                .updatedTime(LocalDateTime.now())
                .eventType(eventType) //변경
                .id(generateId())
                .giftImage(giftImage) //변경
                .image(image)
                .price(price)
                .name(name)
                .build();

        return baseEventProduct;
    }
}
