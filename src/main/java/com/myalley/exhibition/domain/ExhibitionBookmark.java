package com.myalley.exhibition.domain;

import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "exhibition_bookmark")
@SQLDelete(sql = "UPDATE exhibition_bookmark SET is_bookmarked = false WHERE ex_book_id = ?")
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

    @Column(name = "is_bookmarked")
    private boolean isBookmarked;

    @Builder
    public ExhibitionBookmark(Exhibition exhibition, Member member) {
        this.exhibition = exhibition;
        this.member = member;
    }

    public void switchBookmarkStatus() {
        this.isBookmarked = Boolean.TRUE;
    }
}
