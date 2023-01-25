package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogBookmarkRepository extends JpaRepository<BlogBookmark, Long> {
    Optional<BlogBookmark> findByMemberAndBlog(Member member, BlogReview blogReview);
    Page<BlogBookmark> findAllByMember(Member member, Pageable pageable);
    void deleteAllByBlog(BlogReview blogReview);
}
