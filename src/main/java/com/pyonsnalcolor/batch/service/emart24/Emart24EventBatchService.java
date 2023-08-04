package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.service.EventBatchService;
import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("Emart24Event")
@Slf4j
public class Emart24EventBatchService extends EventBatchService {

    private static final String EMART_EVENT_URL_TEMPLATE = "http://www.emart24.co.kr/goods/event?search=&page=%s&category_seq=&align=";
    private static final int TIMEOUT = 20000;

    @Autowired
    public Emart24EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        return BatchExceptionUtil.handleException(this::getProducts);
    }

    private List<BaseEventProduct> getProducts() {
        List<BaseEventProduct> results = new ArrayList<>();
        Elements productElements;
        int curPage = 1;
        do {
            String url = String.format(EMART_EVENT_URL_TEMPLATE, curPage);
            Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(url, TIMEOUT);
            productElements = document.getElementsByClass("itemWrap");

            results.addAll(parseProductsData(productElements));
            curPage++;
        } while (productElements.size() > 0);

        return results;
    }

    private List<BaseEventProduct> parseProductsData(Elements elements) {
        List<BaseEventProduct> results = new ArrayList<>();

        for (Element element : elements) {
            BaseEventProduct baseEventProduct = convertToBaseEventProduct(element);
            results.add(baseEventProduct);
        }
        return results;
    }

    private BaseEventProduct convertToBaseEventProduct(Element element) {
        String giftImage = parseGiftImage(element);
        EventType eventType = parseEventType(element);
        String name = parseName(element);
        String price = element.getElementsByClass("price").get(0).text().split(" ")[0].replaceAll(",", "");
        int parsedPrice = Integer.parseInt(price);

        String originPrice = parseOriginPrice(element);
        Integer parsedOriginPrice = null;
        if (originPrice != null) {
            originPrice = originPrice.replaceAll(",", "");
            parsedOriginPrice = Integer.parseInt(originPrice);
        }
        String image = element.getElementsByTag("img").attr("src");
        Category category = Filter.matchEnumTypeByProductName(Category.class, name);

        return BaseEventProduct.builder()
                .id(generateId())
                .originPrice(parsedOriginPrice)
                .storeType(StoreType.EMART24)
                .eventType(eventType)
                .giftImage(giftImage)
                .giftPrice(null)
                .giftTitle(null)
                .image(image)
                .price(parsedPrice)
                .name(name)
                .category(category)
                .build();
    }

    private String parseGiftImage(Element productElement) {
        Elements giftElement = productElement.select("div.dumgift");
        String giftImage = null;
        if(giftElement.size() > 0) {
            giftImage = giftElement.get(0).getElementsByTag("img").attr("src");
        }

        return giftImage;
    }

    private String parseOriginPrice(Element productElement) {
        String originPrice = null;
        Elements originPriceElement = productElement.getElementsByClass("priceOff");
        if(originPriceElement.size() > 0) {
            originPrice = originPriceElement.get(0).text();
            return originPrice.split(" ")[0];
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
            case "twopl":
                return EventType.TWO_TO_ONE;
            case "onepl":
                return EventType.ONE_TO_ONE;
            default:
                throw new IllegalArgumentException(eventName + "is not support");
        }
    }
}
