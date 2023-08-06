package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.util.BatchExceptionUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;

public interface CuDescriptionBatch {

    String CU_DESCRIPTION_PAGE_URL = "https://cu.bgfretail.com/product/view.do";
    int TIMEOUT = 15000;

    default String getDescription(Element element, String productType) {
        String productCode = getProductCode(element);
        return BatchExceptionUtil.handleException(() -> fetchDescriptionByProductCode(productType, productCode));
    }

    private String fetchDescriptionByProductCode(String productType, String productCode) {
        String detailPage = UriComponentsBuilder
                .fromUriString(CU_DESCRIPTION_PAGE_URL)
                .queryParam("category", productType)
                .queryParam("gdIdx", productCode)
                .build()
                .toString();
        Document document = BatchExceptionUtil.getDocumentByUrlWithTimeout(detailPage, TIMEOUT);
        Elements elements = document.select("ul.prodExplain li");
        return elements.text();
    }

    default String getProductCode(Element element) {
        String originProductElement = element.select("a").first().attr("href");
        return originProductElement.split("\\(")[1].split("\\)")[0];
    }
}