package com.pyonsnalcolor.push.repository;

import com.pyonsnalcolor.push.PushProductStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushProductStoreRepository extends JpaRepository<PushProductStore, Long>{
}