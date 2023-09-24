package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.exception.PyonsnalcolorProductException;
import com.pyonsnalcolor.member.dto.FavoriteRequestDto;
import com.pyonsnalcolor.member.entity.Favorite;
import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.FavoriteRepository;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.product.ProductFactory;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.CommonErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final ProductFactory productFactory;

    public Slice<String> getProductIdsOfFavorite(Pageable pageable, Long memberId, ProductType productType) {
        return favoriteRepository.getFavoriteByMemberIdAndProductType(pageable, memberId, productType)
                .map(Favorite::getProductId);
    }

    public List<String> getProductIdsOfFavorite(Long memberId, ProductType productType) {
        return favoriteRepository.getFavoriteByMemberIdAndProductType(memberId, productType)
                .stream().map(Favorite::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Page<ProductResponseDto> updateProductsIfFavorite(Page<ProductResponseDto> products,
                                                             ProductType productType,
                                                             Long memberId) {
        List<String> favoriteProductIds = getProductIdsOfFavorite(memberId, productType);

        return products.map(
                p -> {
                    if (favoriteProductIds.contains(p.getId())) {
                        p.setFavoriteTrue();
                    }
                    return p;
                });
    }

    public ProductResponseDto updateProductIfFavorite(ProductResponseDto product,
                                                      ProductType productType,
                                                      Long memberId) {
        List<String> favoriteProductIds = getProductIdsOfFavorite(memberId, productType);

        if (favoriteProductIds.contains(product.getId())) {
            product.setFavoriteTrue();
        }
        return product;
    }

    public void createFavorite(Long memberId, FavoriteRequestDto favoriteRequestDto) {
        ProductType productType = favoriteRequestDto.getProductType();
        ProductService productService = productFactory.getService(productType);
        productService.validateProductTypeOfProduct(favoriteRequestDto.getProductId());
        String productId = favoriteRequestDto.getProductId();

        Member member = memberRepository.getReferenceById(memberId);
        Favorite favorite = Favorite.builder()
                .member(member)
                .productId(productId)
                .productType(productType)
                .build();

        if (favoriteRepository.findByProductIdAndMemberId(productId, memberId).isPresent()) {
            throw new PyonsnalcolorProductException(FAVORITE_PRODUCT_ALREADY_EXIST);
        }
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long memberId, FavoriteRequestDto favoriteRequestDto) {
        ProductService productService = productFactory.getService(favoriteRequestDto.getProductType());
        productService.validateProductTypeOfProduct(favoriteRequestDto.getProductId());
        String productId = favoriteRequestDto.getProductId();

        Favorite favorite = favoriteRepository.findByProductIdAndMemberId(productId, memberId)
                .orElseThrow(() -> new PyonsnalcolorProductException(FAVORITE_PRODUCT_NOT_EXIST));
        favoriteRepository.delete(favorite);
    }
}