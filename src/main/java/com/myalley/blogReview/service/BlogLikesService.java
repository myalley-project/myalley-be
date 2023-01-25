package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.repository.BlogLikesRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogLikesService {
    private final BlogLikesRepository likesRepository;

    public Boolean findLikes(BlogReview blog, Member member){
        if(blog.getMember().getMemberId()==member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
        BlogLikes like = likesRepository.findByMemberAndBlog(member,blog).orElseGet(() -> new BlogLikes(member,blog));
        like.changeLikesStatus();
        likesRepository.save(like);
        return like.getIsDeleted();
    }

    public Page<BlogLikes> retrieveMyBlogLikes(Member member, Integer pageNo){
        if(pageNo == null)
            pageNo = 0;
        PageRequest pageRequest = PageRequest.of(pageNo,6, Sort.by("id").descending());
        return likesRepository.findAllByMember(member,pageRequest);
    }

    @Transactional
    public void removeBlogAllLikes(BlogReview blogReview){
        likesRepository.deleteAllByBlog(blogReview);
    }
}
