package com.myalley.member.repository;

import com.myalley.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);

    Member findByNickname(String nickname);
}
