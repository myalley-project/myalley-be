package com.myalley.member_deleted.repository;

import com.myalley.member_deleted.domain.MemberDeleted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDeletedRepository extends JpaRepository<MemberDeleted,Long> {
}
