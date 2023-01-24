package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogImageRepository;
import com.myalley.blogReview.repository.BlogReviewRepository;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogImageService {
    private final BlogImageRepository blogImageRepository;

    private final S3Service s3Service;

    public void addBlogImageList(HashMap<String,String> map, BlogReview blogReview){ //List<BlogImage>
        map.forEach((fileName,S3url)->{
            addBlogImage(fileName,S3url,blogReview);
        });
    }
    public void createNewBlogImage(BlogReview blogReview, MultipartFile image) throws IOException {//(Long blogId
        //BlogReview blogReview = blogReviewService.findBlogReview(blogId); //
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

    public void removeBlogImage(BlogReview blogReview,Long imageId){//(Long blogId,Long imageId){
        //BlogReview blogReview = blogReviewService.findBlogReview(blogId); //
        BlogImage foundImage = retrieveBlogImage(blogReview,imageId);
        s3Service.deleteBlogImage(foundImage.getFileName());
        blogImageRepository.delete(foundImage);
    }

    public List<String> removeBlogAllImages(BlogReview blogReview){
        List<BlogImage> blogImageList = blogImageRepository.findAllByBlog(blogReview);
        if(CollectionUtils.isEmpty(blogImageList))
            return null;
        List<String> fileNames = new ArrayList<>();
        for(BlogImage blogImage : blogImageList) {
            blogImageRepository.delete(blogImage);
            fileNames.add(blogImage.getFileName());
        }
        return fileNames;
    }

    private BlogImage retrieveBlogImage(BlogReview blogReview, Long imageId){
        BlogImage image = blogImageRepository.findByIdAndBlog(imageId,blogReview).orElseThrow(() -> {
                throw new CustomException(BlogReviewExceptionType.IMAGE_NOT_FOUND);
        });
        return image;
    }

    /*
    public List<BlogImage> retrieveBlogImages(Long blogId){
        //blogReviewService
        List<BlogImage> images = blogImageRepository.findAllByBlog(blogId);
        if(images.isEmpty())
            throw new CustomException(BlogReviewExceptionType.IMAGE_NOT_FOUND);
        return images;
    }

 */
}
