package com.myalley.blogReview.repository;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlogLikesRepository extends JpaRepository<BlogLikes, Long> {
    @Query(value="select * from blog_likes bl where bl.member_id=?1 and bl.blog_id=?2", nativeQuery = true)
    Optional<BlogLikes> selectLike(Long memberId, Long blogId);
    Page<BlogLikes> findAllByMember(Member member, Pageable pageable);
    void deleteAllByBlog(BlogReview blogReview);
}
