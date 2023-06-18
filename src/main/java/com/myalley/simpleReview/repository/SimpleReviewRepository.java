package com.myalley.simpleReview.repository;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SimpleReviewRepository extends JpaRepository<SimpleReview, Long> {
    Page<SimpleReview> findAllByExhibition(Exhibition exhibition, Pageable pageable);
    Page<SimpleReview> findAllByMember(Member member, Pageable pageable);
    @Query(value="select * from simple_review br where br.exhibition_id=?1",nativeQuery = true)
    List<SimpleReview> findAllByExhibitionId(Long exhibitionId);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="delete from simple_review sr where sr.simple_id in ?1",nativeQuery = true)
    void removeSimpleReviewByIdList(List<Long> simpleIdList);
}
