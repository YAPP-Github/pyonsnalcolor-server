package com.pyonsnalcolor.push.repository;

import com.pyonsnalcolor.push.PushKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushKeywordRepository extends JpaRepository<PushKeyword, Long> {
}
