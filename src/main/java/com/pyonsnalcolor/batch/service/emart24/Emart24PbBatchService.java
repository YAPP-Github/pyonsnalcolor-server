package com.pyonsnalcolor.batch.service.emart24;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Service("Emart24Pb")
@Slf4j
public class Emart24PbBatchService extends PbBatchService {
    private static final String EMART_PB_URL_TEMPLATE = "http://www.emart24.co.kr/goods/pl?search=&page=%s&category_seq=&align=";
    private static final int TIMEOUT = 20000;

    @Autowired
    public Emart24PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        return BatchExceptionUtil.handleException(this::getProducts);
    }

    private List<BasePbProduct> getProducts() {
        List<BasePbProduct> results = new ArrayList<>();
        Elements productElements;
        int curPage = 1;

        do {
            String url = String.format(EMART_PB_URL_TEMPLATE, curPage);
            Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(url, TIMEOUT);
            productElements = document.getElementsByClass("itemWrap");

            results.addAll(parseProductsData(productElements));
            curPage++;
        } while (productElements.size() > 0);

        return results;
    }

    private List<BasePbProduct> parseProductsData(Elements productElements) {
        List<BasePbProduct> results = new ArrayList<>();

        for (Element productElement : productElements) {
            BasePbProduct basePbProduct = convertToBasePbProduct(productElement);
            results.add(basePbProduct);
        }
        return results;
    }

    private BasePbProduct convertToBasePbProduct(Element element) {
        Elements itemTitle = element.getElementsByClass("itemtitle");
        Element itemTitleElement = itemTitle.get(0);
        String name = itemTitleElement.getElementsByTag("a").get(0).text();
        String price = element.getElementsByClass("price").get(0).text().split(" ")[0].replaceAll(",", "");
        int parsedPrice = Integer.parseInt(price);
        String image = element.getElementsByTag("img").attr("src");
        Category category = Filter.matchEnumTypeByProductName(Category.class, name);
        Recommend recommend = Filter.matchEnumTypeByProductName(Recommend.class, name);

        return BasePbProduct.builder()
                .id(generateId())
                .name(name)
                .price(parsedPrice)
                .storeType(StoreType.EMART24)
                .image(image)
                .category(category)
                .recommend(recommend)
                .build();
    }
}
