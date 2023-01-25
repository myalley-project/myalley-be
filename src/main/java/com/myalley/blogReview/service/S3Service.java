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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class S3Service {
    //1. 이미지 업로드
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket; // Bucket 이름
    @Value("${blog-path}")
    private String imageDir; // 저장할 폴더 이름

    private final AmazonS3Client amazonS3Client;
/*
    public HashMap uploadBlogImages(MultipartFile[] images) throws IOException {


        HashMap<String, String> imageInformationMaps = new HashMap<>();

        for(MultipartFile multipartFile: images) {
            //유형 검사
            verifyFileType(multipartFile.getContentType());

            //파일 명 생성
            String fileName = createFileName(multipartFile.getOriginalFilename());

            //같이 담을 정보 넣기
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(multipartFile.getSize());

            // S3에 업로드
            try{
                amazonS3Client.putObject(
                        new PutObjectRequest(S3Bucket, fileName, multipartFile.getInputStream(), objectMetaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
            }catch (IOException e){
                throw new IOException();
            }

            // 접근가능한 URL 가져오기
            String imagePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();

            imageInformationMaps.put(fileName,imagePath);
        }
        return imageInformationMaps;
    }
*/
    //2번째 방법 : 이미지 하나만 등록하는 메서드를 만들고 블로그 글 새로 등록하는 경우 해당 메서드를 이용하는 것으로 진행
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

    public HashMap uploadBlogImages(List<MultipartFile> images) throws IOException {
        HashMap<String, String> imageInformationMaps = new HashMap<>();
        if(!CollectionUtils.isEmpty(images)) {
            for (MultipartFile multipartFile : images) {
                String[] imageInformation = uploadBlogImage(multipartFile);
                imageInformationMaps.put(imageInformation[0], imageInformation[1]);
            }
        }

        //이미지가 하나도 오지 않은 경우 - 일단은 400 : 지원하지 않는 파일 로 처리
        //Q. 이미지를 전혀 등록하지 않아도 되는 것인지? 아니면 하나는 필수로 등록하도록 해야하는 것인지?
        //1. 안 온 경우는 기본 이미지를 업로드 하는 것으로 해야되나
        //- 테이블은 만들지 않고 목록을 보내주는 경우 만약 이미지가 조회되지 않으면 기본 이미지 url을 보내주도록?
        //- 상세페이지는 상관없음
        //if (imageInformationMaps.isEmpty())
        //    throw new CustomException(BlogReviewExceptionType.IMAGE_BAD_REQUEST);
        return imageInformationMaps;
    }

    //2. 이미지 삭제
    public void deleteBlogImage(String fileName){
        amazonS3Client.deleteObject(S3Bucket, fileName);
    }

    public void deleteBlogAllImages(List<String> fileNameList){
        for(String fileName:fileNameList) {
            deleteBlogImage(fileName);
        }
    }
    
    //S3에 이미지 등록시 사용하는 메서드들
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
    //MIME TYPE 검사



}