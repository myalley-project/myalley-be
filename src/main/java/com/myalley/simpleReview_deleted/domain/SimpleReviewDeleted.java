package com.myalley.simpleReview_deleted.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class SimpleReviewDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "simple_deleted_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate viewDate;
    @Column(nullable = false)
    private Integer rate;
    @Column(nullable = false)
    private String content;
    private String time;
    private String congestion;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private Long exhibitionId;

    @Builder
    public SimpleReviewDeleted(LocalDate viewDate, String time, String congestion, Integer rate, String content,
                               LocalDateTime createdAt, LocalDateTime deletedAt, Long memberId, Long exhibitionId){
        this.viewDate = viewDate;
        this.time = time;
        this.congestion = congestion;
        this.rate = rate;
        this.content = content;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.memberId = memberId;
        this.exhibitionId = exhibitionId;
    }
}
