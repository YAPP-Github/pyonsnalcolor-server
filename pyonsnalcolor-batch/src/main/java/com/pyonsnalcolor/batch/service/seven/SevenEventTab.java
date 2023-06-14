package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.EventType;
import com.pyonsnalcolor.batch.model.StoreType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.batch.model.UUIDGenerator.generateId;

@Slf4j
public enum SevenEventTab {

    ONE_TO_ONE(1, 0),
    TWO_TO_ONE(2, 0),
    GIFT(3, 1),
    DISCOUNT(4, 0);

    @Getter
    private int tab;
    @Getter
    private int startPageIndex;

    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";

    SevenEventTab(int tab, int startPageIndex) {
        this.tab = tab;
        this.startPageIndex = startPageIndex;
    }

    public String getDocumentTag() {
        if (this == GIFT) {
            return "div.pic_product";
        }
        return "div.pic_product div.pic_product";
    }

    public List<BaseEventProduct> getPagedProducts(Elements elements) {
        if (this == GIFT) {
            getPagedProductsByGift(elements);
        }
        return elements.stream()
                .map (p -> convertToBaseEventProduct(p, null))
                .collect(Collectors.toList());
    }

    private BaseEventProduct convertToBaseEventProduct(Element element, Element giftElement) {
        String name = element.select("div.name").first().text();
        String image = IMG_PREFIX + element.select("img").first().attr("src");
        String price = element.select("div.price").text();
        EventType eventType = EventType.getEventTypeWithValue(this.name());
        String giftImage = null;
        if (giftElement != null) {
            giftImage = IMG_PREFIX + giftElement.select("img").first().attr("src");
        }

        return BaseEventProduct.builder()
                .name(name)
                .id(generateId())
                .image(image)
                .price(price)
                .giftImage(giftImage)
                .updatedTime(LocalDateTime.now())
                .eventType(eventType)
                .storeType(StoreType.SEVEN_ELEVEN)
                .build();
    }

    private List<BaseEventProduct> getPagedProductsByGift(Elements elements) {
        AtomicInteger counter = new AtomicInteger();
        List<BaseEventProduct> pagedProducts = new ArrayList<>();

        elements.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 2))
                .forEach((classifier, products) -> {
                    Element originElement = products.get(0);
                    Element giftElement = products.get(1);
                    BaseEventProduct baseEventProduct = convertToBaseEventProduct(originElement, giftElement);
                    pagedProducts.add(baseEventProduct);
                });
        return pagedProducts;
    }
}