package com.myalley.blogReview.service;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.repository.BlogImageRepository;
import com.myalley.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BlogImageService {
    private final BlogImageRepository blogImageRepository;

    public void addBlogImages(HashMap<String,String> map,Long blogId){
        map.forEach((fileName,S3url)->{
            BlogImage newImage=BlogImage.builder()
                    .blogId(blogId)
                    .fileName(fileName)
                    .url(S3url)
                    .build();
            blogImageRepository.save(newImage);
        });
    }

    public void deleteBlogImage(Long imageId){
        //blogImageRepository.delete(retrieveBlogImage());
    }

    public BlogImage retrieveBlogImage(Long blogId, Long imageId){
        BlogImage image = blogImageRepository.findByIdAndBlogId(imageId,blogId).orElseThrow(() -> {
                throw new NoSuchElementException();
        });
        return image;
    }
}
