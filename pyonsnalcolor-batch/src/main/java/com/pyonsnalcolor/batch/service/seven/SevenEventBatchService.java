package com.pyonsnalcolor.batch.service.seven;

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
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.batch.model.UUIDGenerator.generateId;

@Service("SevenEvent")
@Slf4j
public class SevenEventBatchService extends EventBatchService {

    private static final String SEVEN_URL = "https://www.7-eleven.co.kr/product/listMoreAjax.asp?intPageSize=10";
    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";
    private static final int TIMEOUT = 5000;

    private enum SevenEventTab {
        ONE_TO_ONE(1, 0),
        TWO_TO_ONE(2, 0),
        GIFT(3, 1),
        DISCOUNT(4, 0);

        private int tab;
        private int startPageIndex;

        SevenEventTab(int tab, int startPageIndex) {
            this.tab = tab;
            this.startPageIndex = startPageIndex;
        }

        private String getDocumentTag() {
            if (this == GIFT) {
                return "div.pic_product";
            }
            return "div.pic_product div.pic_product";
        }
    }

    public SevenEventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        List<BaseEventProduct> products = new ArrayList<>();

        Arrays.stream(SevenEventTab.values())
                .sequential()
                .map(this::getProductsAllTab)
                .filter(Objects::nonNull)
                .forEach(products::addAll);
        return products;
    }

    private List<BaseEventProduct> getProductsAllTab(SevenEventTab sevenEventTab) {
        try {
            return getProductsByTab(sevenEventTab);
        } catch (IOException ioException) {
            log.error("세븐일레븐 이벤트 {}탭의 상품 조회하는 데 실패했습니다.", sevenEventTab, ioException);
        }
        return null;
    }

    private List<BaseEventProduct> getProductsByTab(SevenEventTab sevenEventTab) throws IOException {
        List<BaseEventProduct> products = new ArrayList<>();

        int pageIndex = sevenEventTab.startPageIndex;
        int tab = sevenEventTab.tab;
        while (true) {
            String sevenEventUrl = getSevenEventUrlByPageIndexAndTab(pageIndex, tab);
            Document doc = Jsoup.connect(sevenEventUrl).timeout(TIMEOUT).get();
            Elements elements = doc.select(sevenEventTab.getDocumentTag());

            if (elements.isEmpty()) {
                break;
            }

            List<BaseEventProduct> tmp = elements.stream()
                    .map (p -> convertToBaseEventProduct(p, sevenEventTab))
                    .collect(Collectors.toList());
            products.addAll(tmp);
            pageIndex++;
        }
        return products;
    }

    private BaseEventProduct convertToBaseEventProduct(Element element, SevenEventTab sevenEventTab) {
        String name = element.select("div.name").first().text();
        String image = IMG_PREFIX + element.select("img").first().attr("src");
        String price = element.select("div.price").text();
        EventType eventType = EventType.getEventTypeWithValue(sevenEventTab.name());

        return BaseEventProduct.builder()
                .name(name)
                .id(generateId())
                .image(image)
                .price(price)
                .updatedTime(LocalDateTime.now())
                .eventType(eventType)
                .storeType(StoreType.SEVEN_ELEVEN)
                .build();
    }

    private String getSevenEventUrlByPageIndexAndTab(int pageIndex, int tab) {
        return UriComponentsBuilder
                .fromUriString(SEVEN_URL)
                .queryParam("intCurrPage", pageIndex)
                .queryParam("pTab", tab)
                .build()
                .toString();
    }
}