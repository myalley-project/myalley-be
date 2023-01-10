package com.myalley.exhibition.dto.request;

import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private String youthPrice;
    private String kidPrice;
    private String fileName;
    private String posterUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String webLink;
    private String purpose;
    private String content;
    private String author;

    @Builder
    public ExhibitionUpdateRequest( String title, ExhibitionStatus status, ExhibitionType type,
                                    String space, String adultPrice, String youthPrice, String kidPrice,
                                    String fileName, String posterUrl, LocalDate startDate, LocalDate endDate,
                                    String webLink, String purpose, String content, String author) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.space = space;
        this.adultPrice = adultPrice;
        this.youthPrice = youthPrice;
        this.kidPrice = kidPrice;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.webLink = webLink;
        this.purpose = purpose;
        this.content = content;
        this.author = author;
    }

    public void setId(Long id) {
        this.exhibitionId = id;
    }
}
