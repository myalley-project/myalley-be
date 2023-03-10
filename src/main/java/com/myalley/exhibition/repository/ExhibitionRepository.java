package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {
    @Modifying
    @Query("update Exhibition e set e.viewCount = e.viewCount + 1 where e.id = :id")
    Integer updateViewCount(Long id);

    Page<Exhibition> findByTypeOrStatus(@Param("type") String type, @Param("status") String status, Pageable pageable);

//    @Query("select e from Exhibition e where e.status like %:status% order by e.id desc")
    Page<Exhibition> findByStatusContaining(@Param("status") String status, Pageable pageable);

    Page<Exhibition> findByStatusContainingAndTitleContaining(String status, String keyword, Pageable pageable);

    Page<Exhibition> findByTypeContainingAndStatusContaining(String type, String keyword, PageRequest pageRequest);
}
