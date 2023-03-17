package com.myalley.exhibition.exhibitionImage.controller;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.exhibitionImage.dto.FileRequestDto;
import com.myalley.exhibition.exhibitionImage.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
@Log
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //전시글 등록 시 이미지 업로드
    @PostMapping("/api/exhibitions/images")
    public ResponseEntity uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        log.info("이미지 파일 업로드");
        return new ResponseEntity(imageService.upload(multipartFile), HttpStatus.OK);
    }

}