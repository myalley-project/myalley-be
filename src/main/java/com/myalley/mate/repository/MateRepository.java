package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MateRepository extends JpaRepository<Mate, Long> {
    @Modifying
    @Query("update Mate m set m.viewCount = m.viewCount + 1 where m.id = :id")
    Integer updateViewCount(Long id);

    @Query("select m from Mate m where m.status like %:status% order by m.createdAt desc")
    Page<Mate> findAllByStatus(@Param("status") String status, Pageable pageable);

    Page<Mate> findByMember(Member member, Pageable pageable);
}