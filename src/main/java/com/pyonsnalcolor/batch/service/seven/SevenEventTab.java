package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Slf4j
@Getter
public enum SevenEventTab {

    ONE_TO_ONE(1, 0),
    TWO_TO_ONE(2, 0),
    GIFT(3, 1),
    DISCOUNT(4, 0);

    private int tab;
    private int startPageIndex;

    private static final String SEVEN_DISCOUNT_URL = "https://www.7-eleven.co.kr/product/presentView.asp";
    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";
    private static final int TIMEOUT = 15000;

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

    public List<BaseEventProduct> getPagedProducts(Elements elements, Elements detailPageElements) {
        if (this == GIFT) {
            return getPagedProductsByGift(elements);
        }
        if (this == DISCOUNT) {
            return getPagedProductsByDiscount(elements, detailPageElements);
        }
        return elements.stream()
                .map (p -> convertToBaseEventProductWithSubProduct(p, null))
                .collect(Collectors.toList());
    }

    private BaseEventProduct convertToBaseEventProductWithSubProduct(Element originElement, Element subElement) {
        String name = originElement.select("div.name").first().text();
        String image = IMG_PREFIX + originElement.select("img").first().attr("src");
        String price = originElement.select("div.price").text().replaceAll(",", "");
        int parsedPrice = Integer.parseInt(price);
        EventType eventType = EventType.valueOf(this.name());

        Integer parsedOriginPrice = null;
        if (this == DISCOUNT) {
            String originProductCode = getProductCode(subElement);
            String originPrice = fetchOriginPrice(originProductCode).replaceAll(",", "");
            parsedOriginPrice = Integer.parseInt(originPrice);
        }
        String giftImage = null;
        String giftTitle = null;
        Integer parsedGiftPrice = null;
        if (this == GIFT) {
            giftImage = IMG_PREFIX + subElement.select("img").first().attr("src");
            giftTitle = subElement.select("div.infowrap div.name").text();
            String giftPrice = subElement.select("div.infowrap div.price span").text()
                    .replaceAll(",", "");
            parsedGiftPrice = Integer.parseInt(giftPrice);
        }
        Category category = Filter.matchEnumTypeByProductName(Category.class, name);

        return BaseEventProduct.builder()
                .name(name)
                .id(generateId())
                .image(image)
                .price(parsedPrice)
                .giftImage(giftImage)
                .giftTitle(giftTitle)
                .giftPrice(parsedGiftPrice)
                .originPrice(parsedOriginPrice)
                .eventType(eventType)
                .storeType(StoreType.SEVEN_ELEVEN)
                .category(category)
                .build();
    }

    private List<BaseEventProduct> getPagedProductsByGift(Elements elements) {
        List<BaseEventProduct> pagedProducts = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();

        elements.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 2))
                .forEach((classifier, products) -> {
                    Element originElement = products.get(0);
                    Element giftElement = products.get(1);
                    BaseEventProduct baseEventProduct = convertToBaseEventProductWithSubProduct(originElement, giftElement);
                    pagedProducts.add(baseEventProduct);
                });
        return pagedProducts;
    }

    private List<BaseEventProduct> getPagedProductsByDiscount(Elements elements, Elements detailPageElements) {
        List<BaseEventProduct> products = new ArrayList<>();
        int elementSize = 0;
        while (elements.size() != elementSize) {
            Element element = elements.get(elementSize);
            Element discountElement = detailPageElements.get(elementSize);
            products.add(convertToBaseEventProductWithSubProduct(element, discountElement));
            elementSize++;
        }
        return products;
    }

    private String getProductCode(Element element) {
        String originProductElement = element.select("a").first().attr("href");
        return originProductElement.split("\\'")[1];
    }

    private String fetchOriginPrice(String originProductCode) {
        return BatchExceptionUtil.handleException(() -> getOriginPriceStringByProductCode(originProductCode));
    }

    private String getOriginPriceStringByProductCode(String originProductCode) {
        String originPriceUrl = UriComponentsBuilder
                .fromUriString(SEVEN_DISCOUNT_URL)
                .queryParam("pCd", originProductCode)
                .build()
                .toString();

        Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(originPriceUrl, TIMEOUT);
        Element priceElement = document.selectFirst(".product_price strong");
        String originPrice = priceElement.text()
                .replaceAll(",", "")
                .replaceAll("[^0-9]", "");
        return originPrice;
    }
}