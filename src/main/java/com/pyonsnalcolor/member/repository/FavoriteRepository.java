package com.pyonsnalcolor.member.repository;

import com.pyonsnalcolor.member.entity.Favorite;
import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.product.enumtype.ProductType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Slice<Favorite> getFavoriteByMemberIdAndProductType(Pageable pageable, Long memberId, ProductType productType);

    List<Favorite> getFavoriteByMemberIdAndProductType(Long memberId, ProductType productType);

    Optional<Favorite> findByProductIdAndMemberId(String productId, Long memberId);

}