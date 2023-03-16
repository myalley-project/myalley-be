package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.response.BlogListResponseDto;
import com.myalley.blogReview.repository.BlogBookmarkRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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

    public Boolean switchBlogBookmark(BlogReview blogReview, Member member) {
        if(blogReview.getMember().getMemberId() == member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.BOOKMARK_FORBIDDEN);
        BlogBookmark bookmark = blogBookmarkRepository.selectBookmark(member.getMemberId(), blogReview.getId())
                .orElse(BlogBookmark.builder().blog(blogReview).member(member).build());
        bookmark.changeBookmarkStatus();
        blogBookmarkRepository.save(bookmark);
        return !bookmark.getIsDeleted();
    }

    public boolean findBlogBookmarkByBlogIdAndMemberId(Long blogId, Long memberId) {
        if(memberId != 0) {
            Member member = memberService.verifyMember(memberId);
            Optional<BlogBookmark> blogBookmark = blogBookmarkRepository.selectBookmark(member.getMemberId(), blogId);
            if (blogBookmark.isPresent())
                return !blogBookmark.get().getIsDeleted();
        }
        return false;
    }

    public BlogListResponseDto findMyBookmarkedBlogReviews(Member member, Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("id").descending());
        return BlogListResponseDto.bookmarkFrom(blogBookmarkRepository.findAllByMember(member, pageRequest));
    }

    @Transactional
    public void removeBlogBookmarksByBlogReview(BlogReview blogReview){
        blogBookmarkRepository.deleteAllByBlog(blogReview);
    }
}
