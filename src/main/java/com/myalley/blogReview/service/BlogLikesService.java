package com.myalley.blogReview.service;

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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogLikesService {
    private final BlogLikesRepository likesRepository;
    private final MemberService memberService;

    public Boolean findLikes(BlogReview blogReview, Member member) {
        if(blogReview.getMember().getMemberId()==member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        BlogLikes like = likesRepository.findByMemberAndBlog(member,blogReview)
                .orElseGet(() -> BlogLikes.builder().blog(blogReview).member(member).build());
        like.changeLikesStatus();
        likesRepository.save(like);
        return !like.getIsDeleted();
    }

    public boolean retrieveBlogLikes(BlogReview blogReview, Long memberId) {
        if(memberId != null) {
            Member member = memberService.verifyMember(memberId);
            Optional<BlogLikes> blogLikes = likesRepository.findByMemberAndBlog(member, blogReview);
            if(blogLikes.isPresent())
                return !blogLikes.get().getIsDeleted();
        }
        return false;
    }

    //isDeleted 값이 false인 것 중에 조회하기
    public Page<BlogLikes> retrieveMyBlogLikes(Member member, Integer pageNo){
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0,6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo-1,6, Sort.by("id").descending());
        return likesRepository.findAllByMember(member,pageRequest);
    }

    @Transactional
    public void removeBlogAllLikes(BlogReview blogReview){
        likesRepository.deleteAllByBlog(blogReview);
    }


}
