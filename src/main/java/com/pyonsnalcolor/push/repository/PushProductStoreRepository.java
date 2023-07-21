package com.pyonsnalcolor.push.repository;

import com.pyonsnalcolor.auth.Member;
import com.pyonsnalcolor.product.enumtype.ProductStoreType;
import com.pyonsnalcolor.push.PushProductStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushProductStoreRepository extends JpaRepository<PushProductStore, Long>{

    List<PushProductStore> findByMember(Member member);

    PushProductStore findByMemberAndProductStoreType(Member member, ProductStoreType productStoreType);
}
