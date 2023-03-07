package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlogBookmarkRepository extends JpaRepository<BlogBookmark, Long> {
    @Query(value="select * from blog_bookmark bb where bb.member_id=?1 and bb.blog_id=?2", nativeQuery = true)
    Optional<BlogBookmark> selectBookmark(Long memberId, Long blogId);
    Page<BlogBookmark> findAllByMember(Member member, Pageable pageable);
    void deleteAllByBlog(BlogReview blogReview);
}
