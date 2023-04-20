package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BlogReviewRepository extends JpaRepository<BlogReview, Long> {
    Page<BlogReview> findAll(Pageable pageable); //최신순, 조회수 순
    Page<BlogReview> findAllByTitleContaining(String word, Pageable pageable);
    Page<BlogReview> findAllByMember(Member member, Pageable pageable); //내 글 조회
    Page<BlogReview> findAllByExhibition(Exhibition exhibition, Pageable pageable); //전시에 맞는 글 조회
    @Query(value="select * from blog_review br where br.exhibition_id=?1 and br.is_deleted=0",nativeQuery = true)
    List<BlogReview> findAllByExhibition(Long exhibitionID);

    @Query(value="select * from blog_review br where br.member_id=?1 and br.is_deleted=1",nativeQuery = true, countProjection = "blog_id")
    Page<BlogReview> selectRemovedAll(Member member, Pageable pageable);
    @Query(value="select * from blog_review br where br.blog_id=?1 and br.is_deleted=1",nativeQuery = true)
    Optional<BlogReview> selectRemovedById(Long blogId);
    @Query(value="select * from blog_review br where br.member_id=?1 and br.blog_id in ?2 and br.is_deleted=1",nativeQuery = true)
    List<BlogReview> selectRemovedByIdList(Long memberId, List<Long> blogId);
    //삭제 예정 - 여러개 요청으로 받을 거라서
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="delete from blog_review br where br.blog_id = ?1",nativeQuery = true)
    void removePermanently(Long blogId);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="delete from blog_review br where br.blog_id in ?1",nativeQuery = true)
    void removeListPermanently(List<Long> blogIdList);
}
