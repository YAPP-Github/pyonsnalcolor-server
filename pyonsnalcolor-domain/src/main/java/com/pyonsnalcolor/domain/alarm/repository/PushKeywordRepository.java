package com.pyonsnalcolor.domain.alarm.repository;

import com.pyonsnalcolor.domain.alarm.PushKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushKeywordRepository extends JpaRepository<PushKeyword, Long> {
}
