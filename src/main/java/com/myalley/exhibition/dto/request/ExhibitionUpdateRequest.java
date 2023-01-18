package com.myalley.exhibition.dto.request;

import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringTokenizer;


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
    private String date;
    private String webLink;
    private String content;
    private String author;

    public ExhibitionUpdateRequest( String title, String status, String type,
                                    String space, Integer adultPrice,
                                    String fileName, String posterUrl, String date,
                                    String webLink, String content, String author) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.space = space;
        this.adultPrice = adultPrice;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.date = date;
        this.webLink = webLink;
        this.content = content;
        this.author = author;
    }

    public void setId(Long id) {
        this.exhibitionId = id;
    }
}
