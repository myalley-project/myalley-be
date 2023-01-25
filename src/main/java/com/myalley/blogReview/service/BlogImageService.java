package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.repository.BlogImageRepository;
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

    public void addBlogImageList(List<MultipartFile> images, BlogReview blogReview) throws IOException {
        //HashMap<String,String> map = s3Service.uploadBlogImages(images);
        if (!CollectionUtils.isEmpty(images)) {
            for (MultipartFile imageFile : images) {
                String[] imageInformation = s3Service.uploadBlogImage(imageFile);
                addBlogImage(imageInformation[0],imageInformation[1],blogReview);
                //imageInformationMaps.put(imageInformation[0], imageInformation[1]);
            }
        }
        //map.forEach((fileName,S3url)->{
       //     addBlogImage(fileName,S3url,blogReview);
        //});
    }

    public void createNewBlogImage(BlogReview blogReview, MultipartFile image) throws IOException {
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

    public void removeBlogImage(BlogReview blogReview,Long imageId){
        BlogImage foundImage = retrieveBlogImage(blogReview,imageId);
        s3Service.deleteBlogImage(foundImage.getFileName());
        blogImageRepository.delete(foundImage);
    }

    public void removeBlogAllImages(BlogReview blogReview) {
        List<BlogImage> blogImageList = blogImageRepository.findAllByBlog(blogReview);
        if (!CollectionUtils.isEmpty(blogImageList)) {
            //List<String> fileNames = new ArrayList<>();
            for (BlogImage blogImage : blogImageList) {
                blogImageRepository.delete(blogImage);
                s3Service.deleteBlogImage(blogImage.getFileName());
                //fileNames.add(blogImage.getFileName());
            }
            //if(!CollectionUtils.isEmpty(fileNames))
            //    s3Service.deleteBlogAllImages(fileNames);
        }
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
