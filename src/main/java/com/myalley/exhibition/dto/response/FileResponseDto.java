package com.myalley.exhibition.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponseDto {
    private String filename;
    private String s3Url;

    public FileResponseDto(String filename, String s3Url) {
        this.filename = filename;
        this.s3Url = s3Url;
    }
}
