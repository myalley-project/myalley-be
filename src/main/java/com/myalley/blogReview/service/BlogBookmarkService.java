package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogBookmarkRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogBookmarkService {
    private final BlogBookmarkRepository blogBookmarkRepository;
    private final MemberService memberService;

    public Boolean findBookmark(BlogReview blogReview, Member member) {
        if(blogReview.getMember().getMemberId() == member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_FORBIDDEN);
        BlogBookmark bookmark = blogBookmarkRepository.findByMemberAndBlog(member, blogReview)
                .orElse(BlogBookmark.builder().blog(blogReview).member(member).build());
        bookmark.changeBookmarkStatus();
        blogBookmarkRepository.save(bookmark);
        return !bookmark.getIsDeleted();
    }

    public boolean retrieveBlogBookmark(BlogReview blogReview, Long memberId) {
        if(memberId != null) {
            Member member = memberService.verifyMember(memberId);
            Optional<BlogBookmark> blogBookmark = blogBookmarkRepository.findByMemberAndBlog(member, blogReview);
            if (blogBookmark.isPresent())
                return !blogBookmark.get().getIsDeleted();
        }
        return false;
    }

    @Transactional
    public void removeBlogAllBookmark(BlogReview blogReview){
        blogBookmarkRepository.deleteAllByBlog(blogReview);
    }

    public Page<BlogBookmark> retrieveMyBlogBookmarks(Member member, Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("id").descending());
        return blogBookmarkRepository.findAllByMember(member, pageRequest);
    }
}
