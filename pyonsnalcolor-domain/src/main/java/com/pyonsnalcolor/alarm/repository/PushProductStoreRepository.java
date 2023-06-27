package com.pyonsnalcolor.alarm.repository;

import com.pyonsnalcolor.alarm.PushProductStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushProductStoreRepository extends JpaRepository<PushProductStore, Long>{
}