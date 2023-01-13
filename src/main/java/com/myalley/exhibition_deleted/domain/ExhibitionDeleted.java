package com.myalley.exhibition_deleted.domain;

import com.myalley.common.domain.BaseTime;
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
public class ExhibitionDeleted extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    private String adultPrice;
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
    @Enumerated(value = EnumType.STRING)
    private ExhibitionStatus status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ExhibitionType type;

    @Builder
    public ExhibitionDeleted(String title, String adultPrice, String space, String fileName, String posterUrl,
                             String date, String webLink, String content, String author,
                             Integer viewCount, ExhibitionStatus status, ExhibitionType type) {
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
}
