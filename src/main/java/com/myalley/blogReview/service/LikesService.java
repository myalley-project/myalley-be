package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.blogReview.repository.BlogLikesRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.test_user.TestMember;
import com.myalley.test_user.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final BlogLikesRepository likesRepository;
    private final TestMemberRepository memberRepository;
    private final BlogReviewRepository blogReviewRepository;

    public void findLikes(Long blogId, Long memberId){
        TestMember testMember = memberRepository.findById(memberId).get();
        BlogReview blog = blogReviewRepository.findById(blogId).get();
        if(blog.getTestMember().getId()==memberId)
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        BlogLikes like = likesRepository.findByTestMemberAndBlog(testMember,blog).orElseGet(() -> new BlogLikes(testMember,blog));
        changeLike(like);
    }

    public void changeLike(BlogLikes like){
            like.changeLikesStatus();
            likesRepository.save(like);
            //현재 Likes내의 blog에 casCade = CascadeType.Persist로 놓음
            //blogReviewRepository.save(like.getBlog());
    }

}
