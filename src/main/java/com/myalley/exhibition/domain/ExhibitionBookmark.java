package com.myalley.exhibition.domain;

import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ExhibitionBookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ex_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "exhibitionId")
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private boolean isBookmarked = true;

    @Builder
    public ExhibitionBookmark(Exhibition exhibition, Member member) {
        this.exhibition = exhibition;
        this.member = member;
    }

    public void addBookmark() {
      this.isBookmarked = true;
    }

}
