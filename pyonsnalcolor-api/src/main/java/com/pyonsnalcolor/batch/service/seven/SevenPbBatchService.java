package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.service.PbBatchService;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.repository.PbProductRepository;
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

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("SevenPb")
@Slf4j
public class SevenPbBatchService extends PbBatchService {

    private static final String SEVEN_URL = "https://www.7-eleven.co.kr/product/listMoreAjax.asp?intPageSize=10";
    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";
    private static final String DOC_SELECT_TAG = "div.pic_product div.pic_product";
    private static final int PB_TAB = 5;
    private static final int TIMEOUT = 5000;

    public SevenPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        try {
            return getProducts();
        } catch (Exception e) {
            log.error("세븐일레븐 PB 상품 조회하는 데 실패했습니다.", e);
        }
        return null; // 이후에 에러 처리 관련 수정 - getAllProducts() 호출하는 쪽에 throw
    }

    private List<BasePbProduct> getProducts() throws IOException {
        List<BasePbProduct> products = new ArrayList<>();

        int pageIndex = 0;
        while (true) {
            String pagedSevenPbUrl = getSevenPbUrlByPageIndex(pageIndex);
            Document doc = Jsoup.connect(pagedSevenPbUrl).timeout(TIMEOUT).get();
            Elements elements = doc.select(DOC_SELECT_TAG);

            if (elements.isEmpty()) {
                break;
            }

            List<BasePbProduct> pagedProducts = elements.stream()
                    .map(this::convertToBasePbProduct)
                    .collect(Collectors.toList());
            products.addAll(pagedProducts);
            pageIndex++;
        }
        return products;
    }

    private BasePbProduct convertToBasePbProduct(Element element) {
        String name = element.select("div.name").first().text();
        String image = IMG_PREFIX + element.select("img").first().attr("src");
        String price = element.select("div.price").text();

        return BasePbProduct.builder()
                .id(generateId())
                .name(name)
                .image(image)
                .price(price)
                .updatedTime(LocalDateTime.now())
                .storeType(StoreType.SEVEN_ELEVEN)
                .build();
    }

    private String getSevenPbUrlByPageIndex(int pageIndex) {
        return UriComponentsBuilder
                .fromUriString(SEVEN_URL)
                .queryParam("intCurrPage", pageIndex)
                .queryParam("pTab", PB_TAB)
                .build()
                .toString();
    }
}
