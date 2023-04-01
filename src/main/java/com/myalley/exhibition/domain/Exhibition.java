package com.myalley.exhibition.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "exhibition")
@SQLDelete(sql = "UPDATE exhibition SET is_deleted = true WHERE exhibition_id = ?")
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

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Exhibition(String title, Integer adultPrice, String space, String fileName, String posterUrl,
                      String duration, String webLink, String content, String author,
                      Integer viewCount, String status, String type, Integer bookmarkCount) {
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
        this.duration = request.getDuration();
        this.webLink = request.getWebLink();
        this.content = request.getContent();
        this.author = request.getAuthor();
    }

    public void bookmarkCountUp() {
        this.bookmarkCount ++;
    }

    public void bookmarkCountDown() {
        this.bookmarkCount --;
    }

}

