package com.myalley.simpleReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.dto.request.PutSimpleDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="simple_review")
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="exhibition_id")
    private Exhibition exhibition;

    @Builder
    public SimpleReview(LocalDate viewDate, String time, String congestion, Integer rate,
                        String content, Member member, Exhibition exhibition){
        this.viewDate = viewDate;
        this.time = time;
        this.congestion = congestion;
        this.rate = rate;
        this.content = content;
        this.member = member;
        this.exhibition = exhibition;
    }

    public void updateSimpleReview(PutSimpleDto simpleDto){
        if(simpleDto.getTime() != null)
            this.time = simpleDto.getTime();
        if(simpleDto.getCongestion() != null)
            this.congestion = simpleDto.getCongestion();
        this.rate = simpleDto.getRate();
        this.viewDate = simpleDto.getViewDate();
        this.content = simpleDto.getContent();
    }
}
