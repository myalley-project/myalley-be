package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogImageRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogImageService {
    private final BlogImageRepository blogImageRepository;
    private final S3Service s3Service;

    public void addBlogImageList(List<MultipartFile> images, BlogReview blogReview) throws IOException {
        if (!CollectionUtils.isEmpty(images)) {
            for (MultipartFile imageFile : images) {
                String[] imageInformation = s3Service.uploadBlogImage(imageFile);
                addBlogImage(imageInformation[0],imageInformation[1],blogReview);
            }
        }
    }

    public void createNewBlogImage(BlogReview blogReview, Member member, MultipartFile image) throws IOException {
        if(blogReview.getMember().getMemberId()!= member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.IMAGE_FORBIDDEN);
        String[] information = s3Service.uploadBlogImage(image);
        addBlogImage(information[0],information[1],blogReview);
    }

    public void addBlogImage(String fileName, String S3url, BlogReview blogReview){
        BlogImage newImage=BlogImage.builder()
                .fileName(fileName)
                .url(S3url)
                .build();
        newImage.setBlog(blogReview);
        blogImageRepository.save(newImage);
    }

    public void removeBlogImage(BlogReview blogReview, Member member, Long imageId){
        if(blogReview.getMember().getMemberId()!= member.getMemberId())
            throw new CustomException(BlogReviewExceptionType.IMAGE_FORBIDDEN);
        BlogImage foundImage = retrieveBlogImage(blogReview,imageId);
        s3Service.deleteBlogImage(foundImage.getFileName());
        blogImageRepository.delete(foundImage);
    }

    public void removeBlogAllImages(BlogReview blogReview) {
        List<BlogImage> blogImageList = blogImageRepository.findAllByBlog(blogReview);
        if (!CollectionUtils.isEmpty(blogImageList)) {
            for (BlogImage blogImage : blogImageList) {
                blogImageRepository.delete(blogImage);
                s3Service.deleteBlogImage(blogImage.getFileName());
            }
        }
    }

    private BlogImage retrieveBlogImage(BlogReview blogReview, Long imageId){
        BlogImage image = blogImageRepository.findByIdAndBlog(imageId,blogReview).orElseThrow(() -> {
                throw new CustomException(BlogReviewExceptionType.IMAGE_NOT_FOUND);
        });
        return image;
    }
}
