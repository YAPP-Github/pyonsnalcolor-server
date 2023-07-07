package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.service.EventBatchService;
import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service("SevenEvent")
@Slf4j
public class SevenEventBatchService extends EventBatchService {

    private static final String SEVEN_URL = "https://www.7-eleven.co.kr/product/listMoreAjax.asp?intPageSize=10";
    private static final int TIMEOUT = 5000;

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

    public List<BaseEventProduct> getProductsByTab(SevenEventTab sevenEventTab) throws IOException {
        List<BaseEventProduct> products = new ArrayList<>();

        int pageIndex = sevenEventTab.getStartPageIndex();
        int tab = sevenEventTab.getTab();
        while (true) {
            String sevenEventUrl = getSevenEventUrlByPageIndexAndTab(pageIndex, tab);
            Document document = Jsoup.connect(sevenEventUrl).timeout(TIMEOUT).get();
            Elements elements = document.select(sevenEventTab.getDocumentTag());

            if (elements.isEmpty()) {
                break;
            }
            Document detailPageDocument = Jsoup.connect(sevenEventUrl).timeout(TIMEOUT).get();
            Elements detailPageElements = detailPageDocument.select("a.btn_product_01");

            List<BaseEventProduct> pagedProducts = sevenEventTab.getPagedProducts(elements, detailPageElements);
            products.addAll(pagedProducts);
            pageIndex += 1;
        }
        return products;
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