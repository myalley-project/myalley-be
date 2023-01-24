package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogBookmarkRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogBookmarkService {
    private final BlogBookmarkRepository blogBookmarkRepository;
    private final BlogReviewService blogReviewService;

    public void createBookmark(Long blogId, Member member){
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        if(blogReview.getMember().getMemberId() == member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_FORBIDDEN);
        if(blogBookmarkRepository.findByMemberAndBlog(member,blogReview).isPresent())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_BAD_REQUEST);
        BlogBookmark newBlogBookmark = new BlogBookmark(member,blogReview, LocalDateTime.now());
        blogReview.increaseBookmarkCount();
        blogBookmarkRepository.save(newBlogBookmark);
    }

    public void deleteBookmark(Long blogId, Member member){
        BlogReview blogReview = blogReviewService.findBlogReview(blogId);
        BlogBookmark bookmark = blogBookmarkRepository.findByMemberAndBlog(member,blogReview).orElseThrow(() -> {
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_BAD_REQUEST);
        });
        bookmark.getBlog().decreaseBookmarkCount();
        blogBookmarkRepository.delete(bookmark);
    }
}
