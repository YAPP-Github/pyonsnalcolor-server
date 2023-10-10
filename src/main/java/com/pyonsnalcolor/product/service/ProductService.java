package com.pyonsnalcolor.product.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.entity.Review;
import com.pyonsnalcolor.product.entity.UUIDGenerator;
import com.pyonsnalcolor.product.enumtype.*;
import com.pyonsnalcolor.product.repository.BasicProductRepository;
import com.pyonsnalcolor.product.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.pyonsnalcolor.exception.model.CommonErrorCode.INVALID_PRODUCT_TYPE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public abstract class ProductService {

    protected final BasicProductRepository basicProductRepository;
    protected final MongoTemplate mongoTemplate;
    private final ImageRepository imageRepository;

    @Transactional
    public ProductResponseDto getProductById(String id) {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository.findById(id).get();
        baseProduct.increaseViewCount();
        basicProductRepository.save(baseProduct);
        return baseProduct.convertToDto();
    }

    protected abstract void validateProductFilterCodes(List<Integer> filterList);

    public abstract <T extends ProductResponseDto> Page<T> getPagedProductsByFilter(
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

    //리뷰 좋아요
    @Transactional
    public void likeReview(String productId, String reviewId) throws Throwable {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository
                .findById(productId)
                .orElseThrow(NoSuchElementException::new);

        Review review = baseProduct.getReviews().stream().filter(
                        r -> r.getReviewId().equals(reviewId)
                ).findFirst()
                .orElseThrow(NoSuchElementException::new);

        review.likeReview();

        basicProductRepository.save(baseProduct);
    }

    //리뷰 싫어요
    @Transactional
    public void hateReview(String productId, String reviewId) throws Throwable {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository
                .findById(productId)
                .orElseThrow(NoSuchElementException::new);

        Review review = baseProduct.getReviews().stream().filter(
                        r -> r.getReviewId().equals(reviewId)
                ).findFirst()
                .orElseThrow(NoSuchElementException::new);

        review.hateReview();

        basicProductRepository.save(baseProduct);
    }

    //리뷰 등록
    @Transactional
    public void registerReview(MultipartFile image, ReviewDto reviewDto, String productId) throws Throwable {
        BaseProduct baseProduct = (BaseProduct) basicProductRepository
                .findById(productId)
                .orElseThrow(NoSuchElementException::new);

        String filePath = "None";

        if (!image.isEmpty()) {
            filePath = imageRepository.uploadImage(image);
        }

        Review review = new Review().builder()
                .reviewId(UUIDGenerator.generateId())
                .contents(reviewDto.getContents())
                .image(filePath)
                .quality(reviewDto.getQuality())
                .score(reviewDto.getScore())
                .taste(reviewDto.getTaste())
                .valueForMoney(reviewDto.getValueForMoney())
                .writerId(reviewDto.getWriterId())
                .writerName(reviewDto.getWriterName())
                .updatedTime(LocalDateTime.now())
                .createdTime(LocalDateTime.now())
                .hateCount(0L)
                .likeCount(0L)
                .build();

        baseProduct.addReview(review);

        basicProductRepository.save(baseProduct);
    }

    public Slice<ProductResponseDto> getProductsOfFavoriteByIds(Slice<String> productIds) {
        return productIds
                .map(p -> {
                    BaseProduct baseProduct = (BaseProduct) basicProductRepository.findById(p).get();
                    ProductResponseDto productResponseDto = baseProduct.convertToDto();
                    productResponseDto.setFavoriteTrue();
                    return productResponseDto;
                });
    }

    public void validateProductTypeOfProduct(String id) {
        if (basicProductRepository.findById(id).isEmpty()) {
            throw new PyonsnalcolorProductException(INVALID_PRODUCT_TYPE);
        }
    }
}
