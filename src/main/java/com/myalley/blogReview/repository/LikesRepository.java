package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
