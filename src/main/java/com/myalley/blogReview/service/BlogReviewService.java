package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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
    //private final S3Service s3Service;

    public void createBlog(BlogReview blogReview, Member member, Long exhibitionId,
                                 List<MultipartFile> images) throws IOException {
        blogReview.setMember(member);
        blogReview.setExhibition(exhibitionService.verifyExhibition(exhibitionId));
        BlogReview newBlog = blogReviewRepository.save(blogReview);
        //HashMap<String,String> map = s3Service.uploadBlogImages(images);
        //if(!map.isEmpty())
        blogImageService.addBlogImageList(images, newBlog);
    }


    public BlogReview retrieveBlogReview(Long blogId){
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        BlogReview updateBlog = blogReviewRepository.save(blog);
        return updateBlog;
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
        if(pageNo == null)
            pageNo = 0;
        PageRequest pageRequest = PageRequest.of(pageNo, 6, Sort.by("id").descending());
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
        blogReviewDeletedService.addDeletedBlogReview(pre);
        //List<String> fileNameList = blogImageService.removeBlogAllImages(pre);
        //if(!CollectionUtils.isEmpty(fileNameList))
        //    s3Service.deleteBlogAllImages(fileNameList);
        blogImageService.removeBlogAllImages(pre);
        bookmarkService.removeBlogAllBookmark(pre);
        likesService.removeBlogAllLikes(pre);
        blogReviewRepository.delete(pre);
    }
    
    //1. 존재하는 글인지 확인
    public BlogReview findBlogReview(Long blogId){
        BlogReview blog = blogReviewRepository.findById(blogId).orElseThrow(() -> { //404 : 존재 하지 않는 글
            throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        return blog;
    }
    
    //2. 작성자인지 확인
    private BlogReview verifyRequester(Long blogId,Long memberId){
        BlogReview review = blogReviewRepository.findById(blogId).orElseThrow(() -> { //404 : 블로그 글이 조회 되지 않는 경우
           throw new CustomException(BlogReviewExceptionType.BLOG_NOT_FOUND);
        });
        if(!review.getMember().getMemberId().equals(memberId)){ //403 : 권한이 없는 글에 접근하는 경우
            throw new CustomException(BlogReviewExceptionType.BLOG_FORBIDDEN);
        }
        return review;
    }
}
