package com.myalley.exhibition.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.options.DeletionStatus;
import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Exhibition extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String adultPrice;

    private String youthPrice;

    private String kidPrice;

    @Column(nullable = false)
    private String space;

    private String fileName;

    @Lob
    @Column(nullable = false)
    private String posterUrl;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String webLink;

    private String purpose;

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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DeletionStatus isDeleted;

    @Builder
    public Exhibition(String title, String adultPrice, String youthPrice, String kidPrice,
                      String space, String fileName, String posterUrl, LocalDate startDate, LocalDate endDate,
                      String webLink, String purpose, String content, String author, Integer viewCount,
                      ExhibitionStatus status, ExhibitionType type, DeletionStatus isDeleted) {
        this.title = title;
        this.adultPrice = adultPrice;
        this.youthPrice = youthPrice;
        this.kidPrice = kidPrice;
        this.space = space;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.webLink = webLink;
        this.purpose = purpose;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.status = status;
        this.type = type;
        this.isDeleted = isDeleted;
    }

    public void updateInfo(Long id, ExhibitionUpdateRequest request) {
        this.id = id;
        this.title = request.getTitle();
    }
}

