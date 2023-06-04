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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("CuPb")
@Slf4j
public class CuPbBatchService extends PbBatchService {

    private static final String CU_PB_URL= "https://cu.bgfretail.com/product/pbAjax.do?searchCondition=setC&";
    private static final String CU_CATEGORY_PB= "PBG";
    private static final String CU_CATEGORY_CU_ONLY= "CUG";

    @Autowired
    public CuPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        List<BasePbProduct> results = new ArrayList<>();

        try {
            results.addAll(getProductsByCategory(CU_CATEGORY_PB));
            results.addAll(getProductsByCategory(CU_CATEGORY_CU_ONLY));
        } catch (Exception e) {
            log.error("해당 페이지에 접근하지 못합니다. {}", e.getMessage());
        }
        return results;
    }

    @Override
    protected List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> CuProducts) {
        System.out.println("send Cu pb products alarms");
    }

    private List<BasePbProduct> getProductsByCategory(String category) throws IOException {
        List<BasePbProduct> products = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String CU_PB_URL_TMP =CU_PB_URL+ "pageIndex=" + pageIndex + "&searchgubun=" + category;
            Document doc = Jsoup.connect(CU_PB_URL_TMP).timeout(0).get();
            Elements elements = doc.select("a.prod_item");

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
}