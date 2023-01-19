package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogBookmarkRepository extends JpaRepository<BlogBookmark, Long> {
}
