package com.pyonsnalcolor.push.repository;

import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.push.PushKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushKeywordRepository extends JpaRepository<PushKeyword, Long> {

    List<PushKeyword> findByMember(Member member);

    Optional<PushKeyword> findByMemberAndId(Member member, Long id);

    Optional<PushKeyword> findByMemberAndName(Member member, String name);

}
