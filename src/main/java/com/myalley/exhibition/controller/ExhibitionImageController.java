package com.myalley.exhibition.controller;

import com.myalley.exhibition.service.ExhibitionImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log
@RestController
@RequiredArgsConstructor
public class ExhibitionImageController {

    private final ExhibitionImageService exhibitionImageService;

    //전시글 등록 시 이미지 업로드
    @PostMapping("/api/exhibitions/images")
    public ResponseEntity uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        log.info("이미지 파일 업로드");
        return new ResponseEntity(exhibitionImageService.upload(multipartFile), HttpStatus.OK);
    }

}
