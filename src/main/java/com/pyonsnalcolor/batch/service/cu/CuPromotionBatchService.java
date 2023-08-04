package com.pyonsnalcolor.batch.service.cu;

import com.pyonsnalcolor.batch.service.PromotionBatchService;
import com.pyonsnalcolor.exception.PyonsnalcolorBatchException;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.BatchErrorCode.*;
import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Slf4j
@Service("CuPromotion")
public class CuPromotionBatchService extends PromotionBatchService {

    private static final String CU_PROMOTION_URL = "https://cu.bgfretail.com/brand_info/news_listAjax.do";
    private static final String CU_DESCRIPTION_PAGE_URL = "https://cu.bgfretail.com/brand_info/news_view.do";
    private static final String DOC_SELECT_TAG =  "tr";
    private static final String LAST_SELECT_TAG =  "div.table_style01 tbody tr td";
    private static final String LAST_MENT =  "등록된 게시물이 없습니다";
    private static final int TIMEOUT = 5000;

    public CuPromotionBatchService(PromotionRepository promotionRepository) {
        super(promotionRepository);
    }

    @Override
    public List<Promotion> getNewPromotions() {
        try {
            return getPromotions();
        } catch (IllegalArgumentException e) {
            throw new PyonsnalcolorBatchException(INVALID_ACCESS, e);
        } catch (SocketTimeoutException e) {
            throw new PyonsnalcolorBatchException(TIME_OUT, e);
        } catch (IOException e) {
            throw new PyonsnalcolorBatchException(IO_EXCEPTION, e);
        } catch (Exception e) {
            throw new PyonsnalcolorBatchException(BATCH_UNAVAILABLE, e);
        }
    }

    private List<Promotion> getPromotions() throws IOException {
        List<Promotion> promotions = new ArrayList<>();

        int pageIndex = 1;
        while (true) {
            String pagedCuEventUrl = getPromotion(pageIndex);
            Document doc = Jsoup.connect(pagedCuEventUrl).timeout(TIMEOUT).get();
            Elements elements = doc.select(DOC_SELECT_TAG);
            Elements finishedElements = doc.select(LAST_SELECT_TAG);
            String isPromotionNotExist = finishedElements.text();
            if (isPromotionNotExist.contains(LAST_MENT)) {
                break;
            }

            List<Promotion> pagedPromotions = elements.stream()
                    .map(this::convertToPromotion)
                    .map(this::validateAllFieldsNotNull)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            promotions.addAll(pagedPromotions);
            pageIndex += 1;
        }
        return promotions;
    }

    private Promotion convertToPromotion(Element element) {
        if (isOutdatedPromotion(element)) {
            return null;
        }
        Elements thumbnailImageElements = element.select("td img");
        if (thumbnailImageElements.isEmpty()) {
            return null;
        }
        String thumbnailImage = thumbnailImageElements.first().attr("src");
        Elements imageElements = element.select("td a.preview_thum");
        if (imageElements.isEmpty()) {
            return null;
        }
        String detailPageIndexString = imageElements.attr("href").split("\\'")[1].split("\\'")[0];
        int detailPageIndex = Integer.parseInt(detailPageIndexString);
        String image = getImageByDetailPageIndex(detailPageIndex);
        Elements titleElements = element.select("div.preview_conts h3 a");
        if (titleElements.isEmpty()) {
            return null;
        }
        String title = Objects.requireNonNull(titleElements.first()).text();

        return Promotion.builder()
                .id(generateId())
                .title(title)
                .thumbnailImage(thumbnailImage)
                .image(image)
                .storeType(StoreType.CU)
                .build();
    }

    private boolean isOutdatedPromotion(Element element) {
        Elements dateElements = element.select("div.preview_conts dl.date_write dd");
        if (dateElements.isEmpty()) {
            return true;
        }
        String[] dates = Objects.requireNonNull(dateElements.first()).text().split("\\.");
        int elementYear = Integer.parseInt(dates[0]);
        int elementMonth = Integer.parseInt(dates[1]);
        int nowYear = LocalDateTime.now().getYear();
        int nowMonth = LocalDateTime.now().getMonthValue();

        if ((elementYear != nowYear) || (elementMonth != nowMonth)) {
            return true;
        }
        return false;
    }

    private String getPromotion(int pageIndex) {
        return UriComponentsBuilder
                .fromUriString(CU_PROMOTION_URL)
                .queryParam("pageIndex", pageIndex)
                .build()
                .toString();
    }

    private String getImageByDetailPageIndex(int pageIdx) {
        try {
            return getImageBy(pageIdx);
        } catch (IllegalArgumentException e) {
            throw new PyonsnalcolorBatchException(INVALID_ACCESS, e);
        } catch (SocketTimeoutException e) {
            throw new PyonsnalcolorBatchException(TIME_OUT, e);
        } catch (IOException e) {
            throw new PyonsnalcolorBatchException(IO_EXCEPTION, e);
        } catch (Exception e) {
            throw new PyonsnalcolorBatchException(BATCH_UNAVAILABLE, e);
        }
    }

    private String getImageBy(int pageIndex) throws IOException {
        String detailPage = UriComponentsBuilder
                .fromUriString(CU_DESCRIPTION_PAGE_URL)
                .queryParam("idx", pageIndex)
                .build()
                .toString();
        Document doc = Jsoup.connect(detailPage).timeout(TIMEOUT).get();
        Elements imageElements = doc.select("div.relCon div.brand_news div.table02 img");

        String detailImage = null;
        if (imageElements.size() != 0) {
            detailImage = imageElements.first().attr("src");
        }
        return detailImage;
    }
}