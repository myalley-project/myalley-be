package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.DetailBlogReview;
import com.myalley.blogReview_deleted.service.BlogReviewDeletedService;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.member.domain.Member;

import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogReviewService {
    private final BlogReviewRepository blogReviewRepository;
    private final BlogReviewDeletedService blogReviewDeletedService;
    private final ExhibitionService exhibitionService;
    private final BlogImageService blogImageService;
    private final BlogBookmarkService bookmarkService;
    private final BlogLikesService likesService;

    public void createBlog(BlogReview blogReview, Member member, Long exhibitionId,
                                 List<MultipartFile> images) throws IOException {
        blogReview.setMember(member);
        blogReview.setExhibition(exhibitionService.verifyExhibition(exhibitionId));
        BlogReview newBlog = blogReviewRepository.save(blogReview);
        blogImageService.addBlogImageList(images, newBlog);
    }

    public DetailBlogReview retrieveBlogReview(Long blogId, Long memberId){
        DetailBlogReview detail = new DetailBlogReview();
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        detail.setLikesStatus(likesService.retrieveBlogLikes(blog,memberId));
        detail.setBookmarkStatus(bookmarkService.retrieveBlogBookmark(blog,memberId));
        detail.setBlogReview(blogReviewRepository.save(blog));
        return detail;
    }

    public Page<BlogReview> retrieveBlogReviewList(Integer pageNo,String orderType){
        Page<BlogReview> blogReviewList;
        if(pageNo == null)
            pageNo = 0;
        if(orderType!=null && orderType.equals("ViewCount")) {
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("viewCount").descending()
                    .and(Sort.by("id").descending()));
            blogReviewList = blogReviewRepository.findAll(pageRequest);
        } else if(orderType==null || orderType.equals("Recent")){
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
            blogReviewList = blogReviewRepository.findAll(pageRequest);
            System.out.println(blogReviewList.getTotalElements());
        } else{
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        }
        return blogReviewList;
    }

    public Page<BlogReview> retrieveMyBlogReviewList(Member member, Integer pageNo) {
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo, 6, Sort.by("id").descending());
        Page<BlogReview> myBlogReviewList = blogReviewRepository.findAllByMember(member,pageRequest);
        return myBlogReviewList;
    }

    public void updateBlogReview(BlogReview postBlogReview, Long blogId, Member member) {
        BlogReview preBlogReview = verifyRequester(blogId,member.getMemberId());
        preBlogReview.updateReview(postBlogReview);
        blogReviewRepository.save(preBlogReview);
    }

    public void removeBlogReview(Long blogId, Member member){
        BlogReview pre = verifyRequester(blogId,member.getMemberId());
        blogImageService.removeBlogAllImages(pre);
        bookmarkService.removeBlogAllBookmark(pre);
        likesService.removeBlogAllLikes(pre);
        blogReviewDeletedService.addDeletedBlogReview(pre);
        blogReviewRepository.delete(pre);
    }


    //1. 존재하는 글인지 확인
    public BlogReview findBlogReview(Long blogId){
        BlogReview blog = blogReviewRepository.findById(blogId).orElseThrow(() -> {
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        return blog;
    }
    
    //2. 작성자인지 확인
    private BlogReview verifyRequester(Long blogId,Long memberId){
        BlogReview review = blogReviewRepository.findById(blogId).orElseThrow(() -> {
           throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        if(!review.getMember().getMemberId().equals(memberId)){
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }
        return review;
    }
}
