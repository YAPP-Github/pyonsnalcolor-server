package com.pyonsnalcolor.member.repository;

import com.pyonsnalcolor.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthId(String oauthId);

    @Query("select m.refreshToken from Member m where m.oauthId= :oauthId")
    Optional<String> findRefreshTokenByOauthId(@Param("oauthId") String oauthId);

}