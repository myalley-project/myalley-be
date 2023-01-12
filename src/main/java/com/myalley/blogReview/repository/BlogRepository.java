package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
