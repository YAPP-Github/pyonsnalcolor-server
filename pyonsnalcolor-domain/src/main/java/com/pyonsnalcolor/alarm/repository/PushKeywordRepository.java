package com.pyonsnalcolor.alarm.repository;

import com.pyonsnalcolor.alarm.PushKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushKeywordRepository extends JpaRepository<PushKeyword, Long> {
}
