package com.myalley.member.repository;

import com.myalley.member.domain.AdminNo;
import com.myalley.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminNoRepository extends JpaRepository<AdminNo,Long> {

    AdminNo findByAdminNo(Long AdminNo);
}
