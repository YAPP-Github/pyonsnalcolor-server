package com.pyonsnalcolor.service.cu;

import com.pyonsnalcolor.model.BaseEventProduct;
import com.pyonsnalcolor.model.EventType;
import com.pyonsnalcolor.model.StoreType;
import com.pyonsnalcolor.repository.EventProductRepository;
import com.pyonsnalcolor.service.EventBatchService;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.model.UUIDGenerator.generateId;

@Service("CuEvent")
@Slf4j
public class CuEventBatchService extends EventBatchService {

    private static final String CU_URL = "https://cu.bgfretail.com/event/plusAjax.do?";
    private static final String DOC_SELECT_TAG = "a.prod_item";
    private static final int TIMEOUT = 5000;

    public CuEventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {

        try {
            return getProducts();
        } catch (Exception e) {
            log.error("CU 행사 상품 조회하는 데 실패했습니다.", e);
        }
        return null; // 이후에 에러 처리 관련 수정 - getAllProducts() 호출하는 쪽에 throw
    }

    private List<BaseEventProduct> getProducts() throws IOException {
        List<BaseEventProduct> products = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String pagedCuEventUrl = getSevenEventUrlByPageIndex(pageIndex);
            Document doc = Jsoup.connect(pagedCuEventUrl).timeout(TIMEOUT).get();
            Elements elements = doc.select(DOC_SELECT_TAG);

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
                .id((generateId()))
                .eventType(eventType)
                .storeType(StoreType.CU)
                .updatedTime(LocalDateTime.now())
                .build();
    }

    private String getSevenEventUrlByPageIndex(int pageIndex) {
        return UriComponentsBuilder
                .fromUriString(CU_URL)
                .queryParam("pageIndex", pageIndex)
                .build()
                .toString();
    }
}
