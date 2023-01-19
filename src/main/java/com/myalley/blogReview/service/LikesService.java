package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.Likes;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.blogReview.repository.LikesRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.Member;
import com.myalley.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final BlogReviewRepository blogReviewRepository;

    public void findLikes(Long memberId, Long blogId){
        Member member = memberRepository.findById(memberId).get();
        BlogReview blog = blogReviewRepository.findById(blogId).get();
        if(blog.getMember().getId()==memberId)
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        Likes like = likesRepository.findByMemberAndBlog(member,blog).orElseGet(() -> new Likes(member,blog));
        changeLike(like);
    }

    public void changeLike(Likes like){
            like.changeLikesStatus();
            likesRepository.save(like);
            //현재 Likes내의 blog에 casCade = CascadeType.Persist로 놓음
            //blogReviewRepository.save(like.getBlog());
    }

}
