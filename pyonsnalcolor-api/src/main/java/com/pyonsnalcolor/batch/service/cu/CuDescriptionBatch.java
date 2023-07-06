package com.pyonsnalcolor.batch.service.cu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface CuDescriptionBatch {

    String CU_DESCRIPTION_PAGE_URL = "https://cu.bgfretail.com/product/view.do";
    int TIMEOUT = 5000;

    default String getDescription(Element element, String productType) throws Exception {
        String productCode = getProductCode(element);

        try {
            String detailPage = UriComponentsBuilder
                    .fromUriString(CU_DESCRIPTION_PAGE_URL)
                    .queryParam("category", productType)
                    .queryParam("gdIdx", productCode)
                    .build()
                    .toString();
            Document doc = Jsoup.connect(detailPage).timeout(TIMEOUT).get();
            Elements elements = doc.select("ul.prodExplain li");
            return elements.text();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("URL 주소가 유효하지 않습니다.");
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("연결 시간이 초과하였습니다.");
        } catch (IOException e) {
            throw new IOException("연결에 실패하였습니다.");
        }
    }

    default String getProductCode(Element element) {
        String originProductElement = element.select("a").first().attr("href");
        return originProductElement.split("\\(")[1].split("\\)")[0];
    }
}