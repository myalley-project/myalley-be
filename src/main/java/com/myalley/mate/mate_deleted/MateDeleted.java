package com.myalley.mate.mate_deleted;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition_deleted.domain.ExhibitionDeleted;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MateDeleted extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String mateGender;

    @Column(nullable = false)
    private String mateAge;

    @Column(nullable = false)
    private String availableDate;

    @Column(nullable = false)
    private String content;

    private String contact;
    private Integer viewCount;

    //회원 연관관계 추가하기

    @ManyToOne
    @JoinColumn(name = "exhibitionId")
    @JsonBackReference
    private ExhibitionDeleted exhibition;

    @Builder
    public MateDeleted(String title, String status, String mateGender, String mateAge,
                String availableDate, String content, String contact, Integer viewCount, ExhibitionDeleted exhibition) {
        this.title = title;
        this.status = status;
        this.mateGender = mateGender;
        this.mateAge = mateAge;
        this.availableDate = availableDate;
        this.content = content;
        this.contact = contact;
        this.viewCount = viewCount;
        this.exhibition = exhibition;
    }
}
