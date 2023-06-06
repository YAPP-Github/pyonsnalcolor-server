package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
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

@Service("CuPb")
@Slf4j
public class CuPbBatchService extends PbBatchService {

    private static final String CU_PB_URL = "https://cu.bgfretail.com/product/pbAjax.do";
    private static final String CU_CATEGORY_PB = "PBG";
    private static final String CU_CATEGORY_CU_ONLY = "CUG";
    private static final String DOC_SELECT_TAG = "a.prod_item";
    private static final int TIMEOUT = 5000;

    public CuPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts(){
        try {
            return getProductsByCategoryAll();
        } catch (Exception e) {
            log.error("CU PB 상품 조회하는 데 실패했습니다.", e);
        }
        return null; // 이후에 에러 처리 관련 수정 - getAllProducts() 호출하는 쪽에 throw
    }

    @Override
    protected List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> CuProducts) {
        System.out.println("send Cu pb products alarms");
    }

    private List<BasePbProduct> getProductsByCategoryAll() throws IOException {
        List<BasePbProduct> products = new ArrayList<>();
        products.addAll(getProductsByCategory(CU_CATEGORY_PB));
        products.addAll(getProductsByCategory(CU_CATEGORY_CU_ONLY));
        return products;
    }

    private List<BasePbProduct> getProductsByCategory(String category) throws IOException {
        List<BasePbProduct> products = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String pagedCuPbUrl = getCuPbUrlByPageIndexAndCategory(pageIndex, category);
            Document doc = Jsoup.connect(pagedCuPbUrl).timeout(TIMEOUT).get();
            Elements elements = doc.select(DOC_SELECT_TAG);

            if (elements.isEmpty()) {
                break;
            }

            List<BasePbProduct> productsTmp = elements.stream()
                    .map(this::convertToBasePbProduct)
                    .collect(Collectors.toList());
            products.addAll(productsTmp);
            pageIndex++;
        }
        return products;
    }

    private BasePbProduct convertToBasePbProduct(Element element) {
        String name = element.select("div.name").first().text();
        String image = element.select("img.prod_img").first().attr("src");
        String price = element.select("div.price > strong").first().text();

        return BasePbProduct.builder()
                .name(name)
                .image(image)
                .price(price)
                .storeType(StoreType.CU.getName())
                .updatedTime(LocalDateTime.now())
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