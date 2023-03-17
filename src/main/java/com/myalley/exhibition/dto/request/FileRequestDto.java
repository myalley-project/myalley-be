package com.myalley.exhibition.dto.request;

import com.myalley.exhibition.domain.Exhibition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileRequestDto {

    private String fileName;

    public FileRequestDto(Exhibition exhibition) {
        this.fileName = exhibition.getFileName();
    }
}
