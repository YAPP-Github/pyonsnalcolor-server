package com.pyonsnalcolor.push.repository;

import com.pyonsnalcolor.push.PushRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {
}
