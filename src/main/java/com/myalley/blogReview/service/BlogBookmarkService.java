package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogBookmarkRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogBookmarkService {
    private final BlogBookmarkRepository blogBookmarkRepository;

    public void createBookmark(BlogReview blogReview, Member member){
        if(blogReview.getMember().getMemberId() == member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_FORBIDDEN);
        if(blogBookmarkRepository.findByMemberAndBlog(member,blogReview).isPresent())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_BAD_REQUEST);
        BlogBookmark newBlogBookmark = new BlogBookmark(member,blogReview, LocalDateTime.now());
        blogReview.increaseBookmarkCount();
        blogBookmarkRepository.save(newBlogBookmark);
    }

    public void removeBookmark(BlogReview blogReview, Member member){
        BlogBookmark bookmark = blogBookmarkRepository.findByMemberAndBlog(member,blogReview).orElseThrow(() -> {
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_BAD_REQUEST);
        });
        bookmark.getBlog().decreaseBookmarkCount();
        blogBookmarkRepository.delete(bookmark);
    }

    @Transactional
    public void removeBlogAllBookmark(BlogReview blogReview){
        blogBookmarkRepository.deleteAllByBlog(blogReview);
    }

    public Page<BlogBookmark> retrieveMyBlogBookmarks(Member member, Integer pageNo){
        if(pageNo==null)
            pageNo=0;
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("id").descending());
        return blogBookmarkRepository.findAllByMember(member, pageRequest);
    }
}
