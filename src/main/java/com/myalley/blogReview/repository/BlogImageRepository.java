package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogImageRepository extends JpaRepository<BlogImage, Long> {
    Optional<BlogImage> findByIdAndBlogId(Long imageId, Long blogId);
    List<BlogImage> findAllByBlogId(Long blogId);

}
