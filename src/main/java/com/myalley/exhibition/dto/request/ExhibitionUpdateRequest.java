package com.myalley.exhibition.dto.request;

import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionUpdateRequest {
    private Long exhibitionId;
    private String title;
    private ExhibitionStatus status;
    private ExhibitionType type;
    private String space;
    private String adultPrice;
    private String fileName;
    private String posterUrl;
    private String date;
    private String webLink;
    private String content;
    private String author;

    public ExhibitionUpdateRequest( String title, ExhibitionStatus status, ExhibitionType type,
                                    String space, String adultPrice,
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
