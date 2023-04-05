package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.request.BlogRequestDto;
import com.myalley.blogReview.dto.response.BlogDetailResponseDto;
import com.myalley.blogReview.dto.response.BlogListResponseDto;
import com.myalley.exhibition.domain.Exhibition;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogReviewService {
    private final BlogReviewRepository blogReviewRepository;
    private final ExhibitionService exhibitionService;
    private final BlogImageService blogImageService;
    private final BlogBookmarkService bookmarkService;
    private final BlogLikesService likesService;

    public static final String BASIC_LIST = "basic";
    public static final String SELF_LIST = "self";

    @Transactional
    public void createBlog(BlogRequestDto blogRequestDto, Member member, Long exhibitionId,
                           List<MultipartFile> images) throws IOException {
        if(images != null && images.size()>3)
            throw new CustomException(BlogReviewExceptionType.IMAGE_BAD_REQUEST_OVER);
        BlogReview blogReview = BlogReview.builder()
                .title(blogRequestDto.getTitle())
                .content(blogRequestDto.getContent())
                .viewDate(LocalDate.parse(blogRequestDto.getViewDate()))
                .time(blogRequestDto.getTime())
                .congestion(blogRequestDto.getCongestion())
                .transportation(blogRequestDto.getTransportation())
                .revisit(blogRequestDto.getRevisit())
                .member(member)
                .exhibition(exhibitionService.validateExistExhibition(exhibitionId)).build();
        BlogReview newBlog = blogReviewRepository.save(blogReview);
        blogImageService.uploadFileList(images, newBlog);
    }

    @Transactional
    public BlogDetailResponseDto findBlogReviewByBlogId(Long blogId, Long memberId){
        BlogReview blog = validateBlogReview(blogId);
        blog.updateViewCount();
        return BlogDetailResponseDto.of(blog,likesService.findBlogLikesByBlogIdAndMemberId(blogId,memberId),
                bookmarkService.findBlogBookmarkByBlogIdAndMemberId(blogId,memberId));
    }

    public BlogListResponseDto findPagedBlogReviews(Integer pageNo, String orderType, String word){
        PageRequest pageRequest;
        Page<BlogReview> blogReviewList;
        if(pageNo == null)
            pageNo = 0;
        else
            pageNo--;
        if(orderType!=null && orderType.equals("ViewCount")) {
            pageRequest = PageRequest.of(pageNo, 9, Sort.by("viewCount").descending()
                    .and(Sort.by("id").descending()));
        } else if(orderType==null || orderType.equals("Recent")){
            pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
        } else{
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);
        }
        if(word != null)
            blogReviewList = blogReviewRepository.findAllByTitleContaining(word,pageRequest);
        else
            blogReviewList = blogReviewRepository.findAll(pageRequest);
        return BlogListResponseDto.blogOf(blogReviewList,BASIC_LIST);
    }

    public BlogListResponseDto findMyBlogReviews(Member member, Integer pageNo) {
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("id").descending());
        Page<BlogReview> myBlogReviewList = blogReviewRepository.findAllByMember(member,pageRequest);
        return BlogListResponseDto.blogOf(myBlogReviewList,SELF_LIST);
    }

    public BlogListResponseDto findPagedBlogReviewsByExhibitionId(Long exhibitionId, Integer pageNo, String orderType) {
        PageRequest pageRequest;
        Exhibition exhibition = exhibitionService.validateExistExhibition(exhibitionId);
        if(pageNo == null)
            pageNo = 0;
        else
            pageNo--;
        if(orderType!=null && orderType.equals("ViewCount"))
            pageRequest = PageRequest.of(pageNo, 9, Sort.by("viewCount").descending()
                    .and(Sort.by("id")).descending());
        else if(orderType == null || orderType.equals("Recent"))
            pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
        else
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);

        return BlogListResponseDto.blogOf(blogReviewRepository.findAllByExhibition(exhibition,pageRequest),BASIC_LIST);
    }

    @Transactional
    public void updateBlogReview(BlogRequestDto blogRequestDto, Long blogId, Member member) {
        BlogReview preBlogReview = verifyRequester(blogId,member.getMemberId());
        preBlogReview.updateReview(blogRequestDto);
    }

    @Transactional
    public void removeBlogReview(Long blogId, Member member){
        BlogReview pre = verifyRequester(blogId,member.getMemberId());
        bookmarkService.removeBlogBookmarksByBlogReview(pre);
        likesService.removeBlogLikesByBlogReview(pre);
        blogReviewRepository.deleteById(pre.getId());
    }


    //1. 존재하는 글인지 확인
    public BlogReview validateBlogReview(Long blogId){
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
