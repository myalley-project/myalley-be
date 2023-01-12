package com.myalley.blogReview.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.repository.BlogImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class S3Service {
    //1. 이미지 업로드
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket; // Bucket 이름
    @Value("${cloud.aws.s3.dir}")
    private String imageDir; // 저장할 폴더 이름

    private final AmazonS3Client amazonS3Client;
    private final BlogImageRepository blogImageRepository;

    public HashMap uploadBlogImages(MultipartFile[] images) throws Exception {


        HashMap<String, String> imageInformationMaps = new HashMap<>();

        for(MultipartFile multipartFile: images) {
            //유형 검사
            if(!verifyFileType(multipartFile.getContentType()))
                return null;

            //파일 명 생성
            String fileName = createFileName(multipartFile.getOriginalFilename());

            //같이 담을 정보 넣기
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(multipartFile.getSize());

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, fileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            // 접근가능한 URL 가져오기
            String imagePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();


            imageInformationMaps.put(fileName,imagePath);

            //저장
            //System.out.println("** saved file name : "+fileName+", url : "+imagePath);
        }
        return imageInformationMaps;
    }

    //2번째 방법 : 이미지 하나만 등록하는 메서드를 만들고 블로그 글 새로 등록하는 경우 해당 메서드를 이용하는 것으로 진행
    public String[] uploadBlogImage_Ver2(MultipartFile multipartFile) throws Exception {
            if(!verifyFileType(multipartFile.getContentType()))
                return null;

            String fileName = createFileName(multipartFile.getOriginalFilename());

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(multipartFile.getSize());

            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, fileName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();

            return new String[]{fileName,imagePath};
    }

    public HashMap uploadBlogImages_Ver2(MultipartFile[] images) throws Exception {
        HashMap<String, String> imageInformationMaps = new HashMap<>();

        for(MultipartFile multipartFile: images) {
            String[] imageInformation = uploadBlogImage_Ver2(multipartFile);
            imageInformationMaps.put(imageInformation[0],imageInformation[1]);
        }
        return imageInformationMaps;
    }








    //2. 이미지 삭제
    public void deleteBlogImage(String fileName){
        amazonS3Client.deleteObject(S3Bucket, fileName);
    }
    
    //S3에 이미지 등록시 사용하는 메서드들
    public String createFileName(String originalFileName){
        String type = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = imageDir+"/"+UUID.randomUUID().toString().concat(type);
        return fileName;
    }
    public boolean verifyFileType(String type) { //잘못된 형식의 파일을 받았다는 에러를 보내주도록 합시다
        String[] typeList = {"image/jpeg","image/jpg","image/png"};
        List<String> strList = new ArrayList<>(Arrays.asList(typeList));
        if(!strList.contains(type)){
            System.out.println("** 글 등록 실패요...");
            return false;
        }
        return true;
    }
    //MIME TYPE 검사



}