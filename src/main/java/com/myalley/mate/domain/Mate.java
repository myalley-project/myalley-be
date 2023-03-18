package com.myalley.mate.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "mate")
@SQLDelete(sql = "UPDATE mate SET is_deleted = true WHERE mate_id = ?")
@Where(clause = "is_deleted = false")
@Entity
public class Mate extends BaseTime {
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
    @Lob
    private String content;

    private String contact;
    private Integer viewCount;

    private Integer bookmarkCount;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitionId")
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Mate(String title, String status, String mateGender, String mateAge,
                String availableDate, String content, String contact, Integer viewCount,
                Integer bookmarkCount, Exhibition exhibition, Member member) {
        this.title = title;
        this.status = status;
        this.mateGender = mateGender;
        this.mateAge = mateAge;
        this.availableDate = availableDate;
        this.content = content;
        this.contact = contact;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.exhibition = exhibition;
        this.member = member;
    }

    public void updateInfo(Long id, MateUpdateRequest request) {
        this.id = id;
        this.title = request.getTitle();
        this.status = request.getStatus();
        this.mateGender = request.getMateGender();
        this.mateAge = request.getMateAge();
        this.availableDate = request.getAvailableDate();
        this.content = request.getContent();
        this.contact = request.getContact();
    }

    public void updateExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public void bookmarkCountUp() {
        this.bookmarkCount ++;
    }

    public void bookmarkCountDown() {
        this.bookmarkCount --;
    }
}
