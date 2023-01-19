package com.myalley.exhibition.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionDetailResponse {

    private Long id;
    private String title;
    private String status;
    private String type;
    private String space;
    private Integer adultPrice;
    private String posterUrl;
    private String duration;
    private String webLink;
    private String content;
    private String author;
    private Integer viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public static ExhibitionDetailResponse of (Exhibition exhibition) {
        return new ExhibitionDetailResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getStatus(),
                exhibition.getType(), exhibition.getSpace(), exhibition.getAdultPrice(),
                 exhibition.getPosterUrl(), exhibition.getDuration(), exhibition.getWebLink(),
                exhibition.getContent(), exhibition.getAuthor(), exhibition.getViewCount(), exhibition.getCreatedAt());
    }
}

