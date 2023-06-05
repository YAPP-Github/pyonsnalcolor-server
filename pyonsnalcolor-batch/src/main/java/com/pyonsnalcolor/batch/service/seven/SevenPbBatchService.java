package com.pyonsnalcolor.batch.service.seven;

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

@Service("SevenPb")
@Slf4j
public class SevenPbBatchService extends PbBatchService {

    private static final String SEVEN_URL = "https://www.7-eleven.co.kr/product/listMoreAjax.asp?intPageSize=10";
    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";
    private static final int PB_TAB = 5;
    private static final int TIMEOUT = 30000;

    @Autowired
    public SevenPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        try {
            return getProducts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> SevenProducts) {
        System.out.println("send Seven pb products alarms");
    }

    private List<BasePbProduct> getProducts() throws IOException {
        List<BasePbProduct> products = new ArrayList<>();

        int intCurrPage = 0;
        while (true) {
            String SEVEN_ELEVEN_TMP = SEVEN_URL + "&intCurrPage=" + intCurrPage + "&pTab=" + PB_TAB;
            Document doc = Jsoup.connect(SEVEN_ELEVEN_TMP).timeout(TIMEOUT).get();
            Elements elements = doc.select("div.pic_product div.pic_product");

            if (elements.isEmpty()) {
                log.error("해당 페이지에서 찾을 수 있는 편의점 상품을 모두 찾았습니다.");
                break;
            }

            List<BasePbProduct> tmp = elements.stream()
                    .map(this::convertToBasePbProduct)
                    .collect(Collectors.toList());
            products.addAll(tmp);
            intCurrPage++;
        }
        return products;
    }

    private BasePbProduct convertToBasePbProduct(Element element) {
        String name = element.select("div.name").first().text();
        String image = IMG_PREFIX + element.select("img").first().attr("src");
        String price = element.select("div.price").text();

        log.info("name : {}", name);
        log.info("image : {}", image);
        log.info("price : {}", price);


        return BasePbProduct.builder()
                .name(name)
                .image(image)
                .price(price)
                .updatedTime(LocalDateTime.now())
                .storeType(StoreType.SEVEN_ELEVEN.getName())
                .build();
    }
}