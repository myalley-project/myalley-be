package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.DetailBlogReview;
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
                .exhibition(exhibitionService.verifyExhibition(exhibitionId)).build();
        BlogReview newBlog = blogReviewRepository.save(blogReview);
        blogImageService.addBlogImageList(images, newBlog);

        /*
         blogReview.title( requestDto.getTitle() );
        blogReview.content( requestDto.getContent() );
        blogReview.viewDate( LocalDate.parse( requestDto.getViewDate() ) );
        blogReview.time( requestDto.getTime() );

        if(requestDto.getRevisit() == null)
            blogReview.revisit( "모르겠음" );
        else
            blogReview.revisit( requestDto.getRevisit() );
        if(requestDto.getCongestion() == null)
            blogReview.congestion( "기억나지않음" );
        else
            blogReview.congestion( requestDto.getCongestion() );
        if(requestDto.getTransportation() == null)
            blogReview.transportation( "기억나지않음" );
        else
            blogReview.transportation( requestDto.getTransportation() );

        return blogReview.build();
         */
    }

    public DetailBlogReview retrieveBlogReview_ex(Long blogId, Long memberId){
        DetailBlogReview detail = new DetailBlogReview();
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        detail.setLikesStatus(likesService.retrieveBlogLikes(blogId,memberId));
        detail.setBookmarkStatus(bookmarkService.retrieveBlogBookmark(blogId,memberId));
        detail.setBlogReview(blogReviewRepository.save(blog));
        return detail;
    }

    public BlogDetailResponseDto retrieveBlogReview(Long blogId, Long memberId){
        BlogReview blog = findBlogReview(blogId);
        blog.updateViewCount();
        return BlogDetailResponseDto.of(blog,likesService.retrieveBlogLikes(blogId,memberId),
                bookmarkService.retrieveBlogBookmark(blogId,memberId));
    }

    //public Page<BlogReview> retrieveBlogReviewList(Integer pageNo,String orderType){
    public BlogListResponseDto retrieveBlogReviewList(Integer pageNo, String orderType){
        Page<BlogReview> blogReviewList;
        if(pageNo == null)
            pageNo = 0;
        else
            pageNo--;
        if(orderType!=null && orderType.equals("ViewCount")) {
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("viewCount").descending()
                    .and(Sort.by("id").descending()));
            blogReviewList = blogReviewRepository.findAll(pageRequest);
        } else if(orderType==null || orderType.equals("Recent")){
            PageRequest pageRequest = PageRequest.of(pageNo, 9, Sort.by("id").descending());
            blogReviewList = blogReviewRepository.findAll(pageRequest);
        } else{
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);
        }
        return BlogListResponseDto.of(blogReviewList,BASIC_LIST);
    }

    // public Page<BlogReview> searchBlogReviewList(String title, Integer pageNo){
    public BlogListResponseDto searchBlogReviewList(String title, Integer pageNo){
        PageRequest pageRequest;
        if(title == "")
            throw new CustomException(BlogReviewExceptionType.BLOG_BAD_REQUEST);
        if(pageNo == null){
            pageRequest = PageRequest.of(0, 9, Sort.by("id").descending());
        } else{
            pageRequest = PageRequest.of(pageNo-1, 9, Sort.by("id").descending());
        }
        Page<BlogReview> blogReviewList = blogReviewRepository.findAllByTitleContaining(title,pageRequest);
        return BlogListResponseDto.of(blogReviewList,BASIC_LIST);
    }

    //public Page<BlogReview> retrieveMyBlogReviewList(Member member, Integer pageNo) {
    public BlogListResponseDto retrieveMyBlogReviewList(Member member, Integer pageNo) {
        PageRequest pageRequest;
        if(pageNo == null)
            pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        else
            pageRequest = PageRequest.of(pageNo-1, 6, Sort.by("id").descending());
        Page<BlogReview> myBlogReviewList = blogReviewRepository.findAllByMember(member,pageRequest);
        return BlogListResponseDto.of(myBlogReviewList,SELF_LIST);
    }

    public Page<BlogReview> retrieveExhibitionBlogReviewList(Long exhibitionId, Integer pageNo, String orderType) {
        PageRequest pageRequest;
        Exhibition exhibition = exhibitionService.verifyExhibition(exhibitionId);
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
        Page<BlogReview> myBlogReviewList = blogReviewRepository.findAllByExhibition(exhibition,pageRequest);
        return myBlogReviewList;
    }

    @Transactional
    public void updateBlogReview(BlogRequestDto blogRequestDto, Long blogId, Member member) {
        BlogReview preBlogReview = verifyRequester(blogId,member.getMemberId());
        preBlogReview.updateReview(blogRequestDto);
        //blogReviewRepository.save(preBlogReview);
    }

    @Transactional
    public void removeBlogReview(Long blogId, Member member){
        BlogReview pre = verifyRequester(blogId,member.getMemberId());
        blogImageService.removeBlogAllImages(pre);
        bookmarkService.removeBlogAllBookmark(pre);
        likesService.removeBlogAllLikes(pre);
        blogReviewRepository.deleteById(pre.getId());
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
