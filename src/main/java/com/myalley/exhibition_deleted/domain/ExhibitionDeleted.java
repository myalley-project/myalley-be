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
@Table(name = "exhibition_deleted")
@Entity
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
    private String duration;
    private String webLink;

    @Lob
    @Column(nullable = false)
    private String content;

    private String author;

    private Integer viewCount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String type;

    private Integer bookmarkCount;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime deletedAt;

    @Builder
    public ExhibitionDeleted(String title, Integer adultPrice, String space, String fileName, String posterUrl,
                             String duration, String webLink, String content, String author, Integer bookmarkCount,
                             Integer viewCount, String status, String type, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.title = title;
        this.adultPrice = adultPrice;
        this.space = space;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.webLink = webLink;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.status = status;
        this.type = type;
        this.bookmarkCount = bookmarkCount;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
