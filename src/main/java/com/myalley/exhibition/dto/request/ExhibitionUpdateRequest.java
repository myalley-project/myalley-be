package com.myalley.exhibition.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionUpdateRequest {
    private Long exhibitionId;
    private String title;
    private String status;
    private String type;
    private String space;
    private Integer adultPrice;
    private String fileName;
    private String posterUrl;
    private String duration;
    private String webLink;
    private String content;
    private String author;

    public ExhibitionUpdateRequest( String title, String status, String type,
                                    String space, Integer adultPrice,
                                    String fileName, String posterUrl, String duration,
                                    String webLink, String content, String author) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.space = space;
        this.adultPrice = adultPrice;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.webLink = webLink;
        this.content = content;
        this.author = author;
    }

    public void setId(Long id) {
        this.exhibitionId = id;
    }
}
