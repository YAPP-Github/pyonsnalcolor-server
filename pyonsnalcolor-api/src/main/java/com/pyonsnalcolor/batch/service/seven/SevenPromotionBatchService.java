package com.pyonsnalcolor.batch.service.seven;

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
import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.BatchErrorCode.*;
import static com.pyonsnalcolor.exception.model.BatchErrorCode.BATCH_UNAVAILABLE;
import static com.pyonsnalcolor.product.entity.UUIDGenerator.generateId;

@Slf4j
@Service("SevenPromotion")
public class SevenPromotionBatchService extends PromotionBatchService {

    private static final String SEVEN_PROMOTION_URL = "https://www.7-eleven.co.kr/event/listMoreAjax.asp";
    private static final String SEVEN_DESCRIPTION_PAGE_URL = "https://www.7-eleven.co.kr/event/eventView.asp";
    private static final int TIMEOUT = 5000;
    private static final int MAX_PAGE_INDEX = 100;
    private static final String DOC_SELECT_TAG = "a.event_img";
    private static final String IMG_PREFIX = "https://www.7-eleven.co.kr";

    public SevenPromotionBatchService(PromotionRepository promotionRepository) {
        super(promotionRepository);
    }

    @Override
    public List<Promotion> getAllPromotions() {
        try {
            return getPromotionPage();
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

    private List<Promotion> getPromotionPage() throws Exception {
        String pagedCuEventUrl = getPromotionPageUrl();
        Document doc = Jsoup.connect(pagedCuEventUrl).timeout(TIMEOUT).get();
        Elements elements = doc.select(DOC_SELECT_TAG);

        return elements.stream()
                .map(this::convertToPromotion)
                .collect(Collectors.toList());
    }

    private Promotion convertToPromotion(Element element) {
        Elements imageAndTitleElements = element.select("img");
        String title = imageAndTitleElements.first().attr("alt");
        String thumbnailImage = IMG_PREFIX + imageAndTitleElements.first().attr("src");
        String promotionIndexStr = element.attr("href").split("\\'")[1].split("\\'")[0];
        int promotionIndex = Integer.parseInt(promotionIndexStr);
        String image = IMG_PREFIX + getDetailPageImage(promotionIndex);

        return Promotion.builder()
                .id(generateId())
                .title(title)
                .image(image)
                .storeType(StoreType.SEVEN_ELEVEN)
                .thumbnailImage(thumbnailImage)
                .updatedTime(LocalDateTime.now())
                .build();
    }

    private String getPromotionPageUrl() {
        return UriComponentsBuilder
                .fromUriString(SEVEN_PROMOTION_URL)
                .queryParam("intPageSize", MAX_PAGE_INDEX)
                .build()
                .toString();
    }

    private String getDetailPageImage(int pageIdx) {
        try {
            String detailPage = UriComponentsBuilder
                    .fromUriString(SEVEN_DESCRIPTION_PAGE_URL)
                    .queryParam("seqNo", pageIdx)
                    .build()
                    .toString();
            Document doc = Jsoup.connect(detailPage).timeout(TIMEOUT).get();
            Elements elements = doc.select("div.cont_top div.gallery_view img");

            String detailImage = null;
            if (elements.size() != 0) {
                detailImage = elements.first().attr("src");
            }
            return detailImage;
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
}