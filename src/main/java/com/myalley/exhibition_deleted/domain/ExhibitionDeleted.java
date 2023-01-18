package com.myalley.exhibition_deleted.domain;

import com.myalley.exhibition.options.ExhibitionStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "Exhibition_deleted")
public class ExhibitionDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String type;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime deletedAt;

    @Builder
    public ExhibitionDeleted(String title, Integer adultPrice, String space, String fileName, String posterUrl,
                             String date, String webLink, String content, String author,
                             Integer viewCount, String status, String type, LocalDateTime createdAt, LocalDateTime deletedAt) {
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
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
