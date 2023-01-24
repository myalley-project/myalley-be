package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.member.domain.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogLikesRepository extends JpaRepository<BlogLikes, Long> {
    Optional<BlogLikes> findByMemberAndBlog(Member member, BlogReview blogReview);
    Page<BlogLikes> findAllByMember(Member member, Pageable pageable);
}
