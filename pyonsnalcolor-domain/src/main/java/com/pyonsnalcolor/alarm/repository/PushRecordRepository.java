package com.pyonsnalcolor.alarm.repository;

import com.pyonsnalcolor.alarm.PushRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {
}
