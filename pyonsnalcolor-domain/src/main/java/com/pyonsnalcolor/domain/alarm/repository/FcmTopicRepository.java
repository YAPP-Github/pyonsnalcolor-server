package com.pyonsnalcolor.domain.alarm.repository;

import com.pyonsnalcolor.domain.alarm.FcmTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTopicRepository extends JpaRepository<FcmTopic, Long>{
}