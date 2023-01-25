package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.repository.BlogLikesRepository;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogLikesService {
    private final BlogLikesRepository likesRepository;
    private final MemberService memberService; //블로그 조회 시 등록되어있는지 확인하기 위함

    public void createLikes(BlogReview blogReview, Member member){
        if(blogReview.getMember().getMemberId() == member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.LIKES_FORBIDDEN);
        if(likesRepository.findByMemberAndBlog(member, blogReview).isPresent())
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        BlogLikes likes = BlogLikes.builder()
                .blog(blogReview).member(member).createdAt(LocalDateTime.now())
                .build();
        blogReview.increaseLikesCount();
        likesRepository.save(likes);
    }

    public void removeLikes(BlogReview blogReview, Member member){
        BlogLikes likes = likesRepository.findByMemberAndBlog(member,blogReview).orElseThrow(() -> {
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        });
        likes.getBlog().decreaseLikesCount();
        likesRepository.delete(likes);
    }

    public Page<BlogLikes> retrieveMyBlogLikes(Member member, Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0,6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo,6, Sort.by("id").descending());
        return likesRepository.findAllByMember(member,pageRequest);
    }

    @Transactional
    public void removeBlogAllLikes(BlogReview blogReview){
        likesRepository.deleteAllByBlog(blogReview);
    }

    public boolean retrieveBlogLikes(BlogReview blogReview, Long memberId) {
        if(memberId != null) {
            Member member = memberService.verifyMember(memberId);
            if (likesRepository.findByMemberAndBlog(member, blogReview).isPresent())
                return true;
        }
        return false;
    }
}
