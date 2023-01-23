package com.myalley.simpleReview.domain;

import com.myalley.common.domain.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class SimpleReview extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "simple_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate viewDate;
    @Column(nullable = false)
    private Integer rate;
    @Column(nullable = false)
    private String content;
    private String time;
    private String congestion;

    private Long member;
    private Long exhibition;

    @Builder
    public SimpleReview(LocalDate viewDate, String time, String congestion, Integer rate,
                        String content, Long member, Long exhibition){
        this.viewDate = viewDate;
        this.time = time;
        this.congestion = congestion;
        this.rate = rate;
        this.content = content;
        this.member = member;
        this.exhibition = exhibition;
    }
}
