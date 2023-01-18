package com.myalley.exhibition.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Exhibition extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    private Integer adultPrice;
    @Column(nullable = false)
    private String space;
    private String fileName;
    @Lob
    @Column(nullable = false)
    private String posterUrl;

    @Column(nullable = false)
    private String date;
    private String webLink;

    @Column(nullable = false)
    private String content;

    private String author;

    private Integer viewCount;

    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
    private String status;

    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
    private String type;

    @Builder
    public Exhibition(String title, Integer adultPrice, String space, String fileName, String posterUrl,
                      String date, String webLink, String content, String author,
                      Integer viewCount, String status, String type) {
        this.title = title;
        this.adultPrice = adultPrice;
        this.space = space;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.date = date;
        this.webLink = webLink;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.status = status;
        this.type = type;
    }

    public void updateInfo(Long id, ExhibitionUpdateRequest request) {
        this.id = id;
        this.title = request.getTitle();
        this.status = request.getStatus();
        this.type = request.getType();
        this.space = request.getSpace();
        this.adultPrice = request.getAdultPrice();
        this.fileName = request.getFileName();
        this.posterUrl = request.getPosterUrl();
        this.date = request.getDate();
        this.webLink = request.getWebLink();
        this.content = request.getContent();
        this.author = request.getAuthor();
    }

}

