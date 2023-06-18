package com.pyonsnalcolor.domain.alarm.repository;

import com.pyonsnalcolor.domain.alarm.PushRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {
}
