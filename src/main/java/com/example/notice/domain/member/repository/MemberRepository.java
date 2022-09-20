package com.example.notice.domain.member.repository;

import com.example.notice.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
* JpaRepository
* */
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existByUsername(String username);
}
