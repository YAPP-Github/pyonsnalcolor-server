package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorException;
import com.pyonsnalcolor.exception.model.CommonErrorCode;
import com.pyonsnalcolor.exception.model.ErrorCode;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.entity.Review;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.BasicProductRepository;
import com.pyonsnalcolor.util.file.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public abstract class ProductService {

    protected final BasicProductRepository basicProductRepository;
    protected final MongoTemplate mongoTemplate;

    @Transactional
    public ProductResponseDto getProductById(String id) {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository.findById(id).get();
        baseProduct.increaseViewCount();
        basicProductRepository.save(baseProduct);
        return baseProduct.convertToDto();
    }

    protected abstract void validateProductFilterCodes(List<Integer> filterList);

    public <T extends ProductResponseDto> Page<T> getPagedProductsDtoByFilter(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto) {

        return getPagedProductsByFilter(pageable, storeType, productFilterRequestDto)
                .map(p -> (T) p.convertToDto());
    }

    protected abstract <T extends BaseProduct> Page<T> getPagedProductsByFilter(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto);

    protected Aggregation getAggregation(
            Pageable pageable, String storeType, ProductFilterRequestDto productFilterRequestDto
    ) {
        List<Integer> filterList = productFilterRequestDto.getFilterList();
        Criteria criteria = createCriteriaByFilter(storeType, filterList);
        Sort sort = Sorted.findSortByFilterList(filterList);

        return newAggregation(
                match(criteria),
                sort(sort),
                skip(pageable.getOffset()),
                limit(pageable.getPageSize())
        );
    }

    protected Criteria createCriteriaByFilter(String storeType, List<Integer> filterList) {
        List<Recommend> recommends = Filter.findEnumByFilterList(Recommend.class, filterList);
        List<Category> categories = Filter.findEnumByFilterList(Category.class, filterList);
        List<EventType> eventTypes = Filter.findEnumByFilterList(EventType.class, filterList);

        return createFilterCriteria(recommends, categories, eventTypes, storeType);
    }

    private Criteria createFilterCriteria(List<Recommend> recommends, List<Category> categories,
                                          List<EventType> eventTypes, String storeType) {
        Criteria criteria = new Criteria();
        criteria = Filter.getCriteria(recommends, "recommend", criteria);
        criteria = Filter.getCriteria(categories, "category", criteria);
        criteria = Filter.getCriteria(eventTypes, "eventType", criteria);
        criteria = StoreType.getCriteria(storeType, criteria);

        return criteria;
    }

    //리뷰 등록
    public void registerReview(MultipartFile image, ReviewDto reviewDto, String productId) throws Throwable {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository
                .findById(productId)
                .orElseThrow(NoSuchElementException::new);

        String filePath = FileUtils.uploadImage(image, baseProduct.getId());
        if (filePath.isEmpty()) {
            //TODO : 업로드 실패했을 시 처리 필요
            throw new PyonsnalcolorException(CommonErrorCode.SERVER_UNAVAILABLE);
        }

        Review review = new Review().builder()
                .contents(reviewDto.getContents())
                .image(filePath)
                .quality(reviewDto.getQuality())
                .score(reviewDto.getScore())
                .taste(reviewDto.getTaste())
                .valueForMoney(reviewDto.getValueForMoney())
                .build();

        baseProduct.addReview(review);

        basicProductRepository.save(baseProduct);
    }
}
