package com.myalley.exhibition.exhibitionImage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.exhibitionImage.dto.FileResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;
    private static final Tika tika = new Tika();
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${exhibition-path}")
    private String dirName;

    //이미지 파일 업로드
    public FileResponseDto upload(MultipartFile multipartFile) throws IOException {
        validateFileExists(multipartFile);

      try (InputStream inputStream = multipartFile.getInputStream()) {
//          System.out.println("Content Type : " + multipartFile.getContentType());

          //확장자 검사
          if(!multipartFile.isEmpty() && validateImgFile(inputStream)) {
              String newFileName = dirName + "/" + createFilename(multipartFile.getOriginalFilename());
              ObjectMetadata objMeta = new ObjectMetadata();

              objMeta.setContentLength(multipartFile.getInputStream().available());
              amazonS3Client.putObject(bucket, newFileName, multipartFile.getInputStream(), objMeta);
             String s3Url = amazonS3Client.getUrl(bucket, newFileName).toString();

             return new FileResponseDto(newFileName ,s3Url);
          }

          if (!validateImgFile(inputStream)) {
              throw new CustomException(ExhibitionExceptionType.FILE_TYPE_NOT_ACCEPTED);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
        return new FileResponseDto(dirName ,"");
    }

    //이미지 파일 버킷에서 삭제하기
    public void removeFile(String filename) {
        amazonS3Client.deleteObject(bucket, filename);
    }

    //이미지 파일 MIME Type 검사하기
    public boolean validateImgFile(InputStream inputStream) {
        try {
            List<String> notValidTypeList = Arrays.asList("image/jpeg", "image/jpg", "image/png");
            String mimeType = tika.detect(inputStream);
            System.out.println("MimeType : " + mimeType);

            boolean isValid  = notValidTypeList.stream()
                    .anyMatch(notvalidType -> notvalidType.equalsIgnoreCase(mimeType));
            return isValid;

        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ExhibitionExceptionType.FILE_TYPE_NOT_ACCEPTED);
        }
    }

    //파일 첨부됐는지 확인하기
    public void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ExhibitionExceptionType.FILE_NOT_UPLOADED);
        }
    }

    //파일 이름 만들기
    private String createFilename(String filename) {
        return UUID.randomUUID().toString().concat(getFileExt(filename));
    }

    //파일 확장자 추출
    private String getFileExt(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException(ExhibitionExceptionType.FILE_TYPE_NOT_ACCEPTED);
        }
    }
}
