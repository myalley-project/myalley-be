package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MateRepository extends JpaRepository<Mate, Long>, MateRepositoryCustom {
    @Modifying
    @Query("update Mate m set m.viewCount = m.viewCount + 1 where m.id = :id")
    Integer updateViewCount(Long id);

//    @Query(value="select m from Mate m where m.mate_id = :id and m.is_deleted=0", nativeQuery = true)
//    Optional<Mate> findByIdAndIsDeleted(Long id);
}