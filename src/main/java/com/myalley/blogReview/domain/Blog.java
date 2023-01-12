package com.myalley.blogReview.domain;

import com.myalley.blogReview.option.TransportationType;
import com.myalley.blogReview.option.RevisitType;
import com.myalley.blogReview.option.CongestionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate viewDate;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private Integer likeCount;
    private Integer viewCount;

    private Boolean isDeleted;  //삭제여부
    @Column(nullable = false)
    private LocalDate createdAt;
    private LocalDate modifiedAt;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransportationType transportation;  //주차공간
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RevisitType revisit;  //재방문의향
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CongestionType congestion; //혼잡도

    @Column(name = "member_id")
    private Long member;
    @Column(name = "exhibition_id")
    private Long exhibition;

    @Builder
    public Blog(String title, String content, LocalDate viewDate, TransportationType transportation,
                RevisitType revisit, CongestionType congestion, LocalDate createdAt,LocalDate modifiedAt,
                Integer viewCount, Integer likeCount, Boolean isDeleted, Long member, Long exhibition){
        this.title = title;
        this.content = content;
        this.viewDate = viewDate;
        this.transportation = transportation;
        this.revisit = revisit;
        this.congestion = congestion;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isDeleted = isDeleted;
        this.member = member;
        this.exhibition = exhibition;
    }


}
