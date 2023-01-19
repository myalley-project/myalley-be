package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.Likes;
import com.myalley.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberAndBlog(Member member, BlogReview blogReview);
}
