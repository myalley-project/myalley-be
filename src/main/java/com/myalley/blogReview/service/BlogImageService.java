package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogImageRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogImageService {
    private final BlogImageRepository blogImageRepository;

    public void addBlogImageList(HashMap<String,String> map, BlogReview blogReview){ //List<BlogImage>
        map.forEach((fileName,S3url)->{
            addBlogImage(fileName,S3url,blogReview);
        });
    }
    public BlogImage addBlogImage(String fileName, String S3url, BlogReview blogReview){ //원래 void
        BlogImage newImage=BlogImage.builder()
                .fileName(fileName)
                .url(S3url)
                .build();
        blogReview.setImage(newImage);
        return blogImageRepository.save(newImage);
    }

    public void deleteBlogImage(BlogImage image){
        blogImageRepository.delete(image);
    }

    public List<String> deleteBlogAllImages(Long blogId){
        List<BlogImage> blogImageList = retrieveBlogImages(blogId);
        List<String> fileNames = new ArrayList<>();
        for(BlogImage blogImage : blogImageList) {
            deleteBlogImage(blogImage);
            fileNames.add(blogImage.getFileName());
        }
        return fileNames;
    }

    public BlogImage retrieveBlogImage(Long blogId, Long imageId){
        BlogImage image = blogImageRepository.findByIdAndBlogId(imageId,blogId).orElseThrow(() -> {
                throw new CustomException(BlogReviewExceptionType.IMAGE_NOT_FOUND);
        });
        return image;
    }

    public List<BlogImage> retrieveBlogImages(Long blogId){
        List<BlogImage> images = blogImageRepository.findAllByBlogId(blogId);
        if(images.isEmpty())
            throw new CustomException(BlogReviewExceptionType.IMAGE_NOT_FOUND);
        return images;
    }
}
