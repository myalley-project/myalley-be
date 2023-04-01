package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition,Long>, ExhibitionRepositoryCustom {
    @Modifying
    @Query("update Exhibition e set e.viewCount = e.viewCount + 1 where e.id = :id")
    Integer updateViewCount(Long id);

//    @Query(value="select * from Exhibition e where e.exhibition_id = :id and e.is_deleted=0", nativeQuery = true)
//    Optional<Exhibition> findByIdAndIsDeleted(Long id);

}
