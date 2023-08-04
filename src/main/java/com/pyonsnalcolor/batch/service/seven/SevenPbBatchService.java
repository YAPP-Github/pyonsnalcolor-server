package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.service.PbBatchService;
import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.Filter;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.product.enumtype.Recommend;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

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
    private static final int TIMEOUT = 20000;

    public SevenPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    public List<BasePbProduct> getAllProducts() {
        return BatchExceptionUtil.handleException(this::getProducts);
    }

    private List<BasePbProduct> getProducts() {
        List<BasePbProduct> products = new ArrayList<>();

        int pageIndex = 0;
        while (true) {
            String pagedSevenPbUrl = getSevenPbUrlByPageIndex(pageIndex);
            Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(pagedSevenPbUrl, TIMEOUT);
            Elements elements = document.select(DOC_SELECT_TAG);
            if (elements.isEmpty()) {
                break;
            }

            List<BasePbProduct> pagedProducts = elements.stream()
                    .map(this::convertToBasePbProduct)
                    .collect(Collectors.toList());
            products.addAll(pagedProducts);
            pageIndex += 1;
        }
        return products;
    }

    private BasePbProduct convertToBasePbProduct(Element element) {
        String name = element.select("div.name").first().text();
        String image = IMG_PREFIX + element.select("img").first().attr("src");
        String price = element.select("div.price").text().replaceAll(",", "");
        int parsedPrice = Integer.parseInt(price);

        Category category = Filter.matchEnumTypeByProductName(Category.class, name);
        Recommend recommend = Filter.matchEnumTypeByProductName(Recommend.class, name);

        return BasePbProduct.builder()
                .id(generateId())
                .name(name)
                .image(image)
                .price(parsedPrice)
                .storeType(StoreType.SEVEN_ELEVEN)
                .category(category)
                .recommend(recommend)
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