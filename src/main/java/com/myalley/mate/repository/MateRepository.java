package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MateRepository extends JpaRepository<Mate, Long> {
    @Modifying
    @Query("update Mate m set m.viewCount = m.viewCount + 1 where m.id = :id")
    Integer updateViewCount(Long id);
}
