package com.myalley.blogReview.dto.response;

import com.myalley.blogReview.domain.BlogImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String url;

    public static ImageDto from(BlogImage image){
        return new ImageDto(image.getId(), image.getUrl());
    }
}