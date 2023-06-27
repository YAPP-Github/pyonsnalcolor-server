package com.pyonsnalcolor.service.emart24;

import com.pyonsnalcolor.model.BasePbProduct;
import com.pyonsnalcolor.model.StoreType;
import com.pyonsnalcolor.repository.PbProductRepository;
import com.pyonsnalcolor.service.PbBatchService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pyonsnalcolor.model.UUIDGenerator.generateId;

@Service("Emart24Pb")
@Slf4j
public class Emart24PbBatchService extends PbBatchService {
    private static final String EMART_PB_URL_TEMPLATE = "http://www.emart24.co.kr/goods/pl?search=&page=%s&category_seq=&align=";

    @Autowired
    public Emart24PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        try {
            List<BasePbProduct> results = new ArrayList<>();
            Elements productElements;
            int curPage = 1;

            do {
                String url = String.format(EMART_PB_URL_TEMPLATE, curPage);
                Document document = Jsoup.connect(url).get();
                productElements = document.getElementsByClass("itemWrap");

                results.addAll(parseProductsData(productElements));
                curPage++;
            } while (productElements.size() > 0);

            return results;
        } catch (Exception e) {
            //TODO : 임시로 모든 예외에 대해 퉁쳐서 처리. 후에 리팩토링 진행할 것
            log.error("fail getAllProducts", e);
        }
        return Collections.emptyList();
    }

    private List<BasePbProduct> parseProductsData(Elements productElements) {
        List<BasePbProduct> results = new ArrayList<>();

        for (Element productElement : productElements) {
            Elements itemTitle = productElement.getElementsByClass("itemtitle");
            Element element = itemTitle.get(0);
            String name = element.getElementsByTag("a").get(0).text();
            String price = convertToNum(productElement.getElementsByClass("price").get(0).text());
            String image = productElement.getElementsByTag("img").attr("src");

            results.add(convertToBasePbProduct(image, name, price));
        }
        return results;
    }

    private String convertToNum(String price) {
        String priceInfo = price.split(" ")[0];
        String result = priceInfo.replace(",", "");

        return result;
    }

    private BasePbProduct convertToBasePbProduct(String image, String name, String price) {
        BasePbProduct basePbProduct = BasePbProduct.builder()
                .id(generateId())
                .name(name)
                .price(price)
                .storeType(StoreType.EMART24)
                .updatedTime(LocalDateTime.now())
                .image(image)
                .build();

        return basePbProduct;
    }
}
