package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.service.PbBatchService;
import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.Filter;
import com.pyonsnalcolor.product.enumtype.Recommend;
import com.pyonsnalcolor.product.enumtype.StoreType;
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

@Service("CuPb")
@Slf4j
public class CuPbBatchService extends PbBatchService implements CuDescriptionBatch {

    private static final String CU_PB_URL = "https://cu.bgfretail.com/product/pbAjax.do";
    private static final String CU_CATEGORY_PB = "PBG";
    private static final String CU_CATEGORY_CU_ONLY = "CUG";
    private static final String DOC_SELECT_TAG = "a.prod_item";
    private static final int TIMEOUT = 5000;
    private static final String SCHEMA = "https:";

    public CuPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        return BatchExceptionUtil.handleException(this::getProductsByCategoryAll);
    }

    private List<BasePbProduct> getProductsByCategoryAll() {
        List<BasePbProduct> products = new ArrayList<>();
        products.addAll(getProductsByCategory(CU_CATEGORY_PB));
        products.addAll(getProductsByCategory(CU_CATEGORY_CU_ONLY));
        return products;
    }

    private List<BasePbProduct> getProductsByCategory(String category) {
        List<BasePbProduct> products = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String pagedCuPbUrl = getCuPbUrlByPageIndexAndCategory(pageIndex, category);
            Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(pagedCuPbUrl, TIMEOUT);
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
        String image = element.select("img.prod_img").first().attr("src");
        if (!image.contains("http")) {
            image = SCHEMA + image;
        }
        String price = element.select("div.price > strong").first().text().replaceAll(",", "");
        int parsedPrice = Integer.parseInt(price);
        String description = getDescription(element, "product");
        Category category = Filter.matchEnumTypeByProductName(Category.class, name);
        Recommend recommend = Filter.matchEnumTypeByProductName(Recommend.class, name);

        return BasePbProduct.builder()
                .id((generateId()))
                .name(name)
                .image(image)
                .price(parsedPrice)
                .description(description)
                .storeType(StoreType.CU)
                .category(category)
                .recommend(recommend)
                .build();
    }

    private String getCuPbUrlByPageIndexAndCategory(int pageIndex, String category) {
        return UriComponentsBuilder
                .fromUriString(CU_PB_URL)
                .queryParam("searchCondition", "setC")
                .queryParam("pageIndex", pageIndex)
                .queryParam("searchgubun", category)
                .build()
                .toString();
    }
}