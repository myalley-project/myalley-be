package com.myalley.blogReview_deleted.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class BlogReviewDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_deleted_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate viewDate;
    @Column(nullable = false)
    private String time;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private String transportation;
    private String revisit;
    private String congestion;

    @Column(name = "member_id")
    private Long member;
    @Column(name = "exhibition_id")
    private Long exhibition;

    @Builder
    public BlogReviewDeleted(String title, String content, LocalDate viewDate, String time, String transportation,
                      String revisit, String congestion, Integer viewCount, Integer likeCount, Integer bookmarkCount,
                      LocalDateTime createdAt, LocalDateTime deletedAt, Long member, Long exhibition){

        this.title = title;
        this.content = content;
        this.viewDate = viewDate;
        this.time = time;
        this.transportation = transportation;
        this.revisit = revisit;
        this.congestion = congestion;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.bookmarkCount = bookmarkCount;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.member = member;
        this.exhibition = exhibition;
    }
}