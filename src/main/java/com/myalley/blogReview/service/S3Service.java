package com.myalley.blogReview.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;
    @Value("${blog-path}")
    private String imageDir;

    private final AmazonS3Client amazonS3Client;

    //이미지 하나만 등록하는 메서드를 만들고 블로그 글 새로 등록하는 경우 해당 메서드를 이용하는 것으로 진행
    public String[] uploadBlogImage(MultipartFile multipartFile) throws IOException {
            verifyFileType(multipartFile.getContentType());

            String fileName = createFileName(multipartFile.getOriginalFilename());

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(multipartFile.getSize());

        try{
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, fileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        }catch (IOException e){
                        throw new IOException();
        }

            String imagePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();

            return new String[]{fileName,imagePath};
    }

    public void deleteBlogImage(String fileName){
        amazonS3Client.deleteObject(S3Bucket, fileName);
    }

    //S3에 이미지 등록할 때 필요한 메서드
    public String createFileName(String originalFileName){
        String type = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = imageDir+"/"+UUID.randomUUID().toString().concat(type);
        return fileName;
    }
    public void verifyFileType(String type) {
        String[] typeList = {"image/jpeg","image/jpg","image/png"};
        List<String> strList = new ArrayList<>(Arrays.asList(typeList));
        if(!strList.contains(type))
            throw new CustomException(BlogReviewExceptionType.IMAGE_BAD_REQUEST);
    }
}