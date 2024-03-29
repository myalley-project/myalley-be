package com.myalley.mate.domain;

import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Table(name = "mate_bookmark")
@SQLDelete(sql = "UPDATE mate_bookmark SET is_bookmarked = false WHERE mate_book_id = ?")
@Entity
@NoArgsConstructor
public class MateBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mateId")
    private Mate mate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "is_bookmarked")
    private boolean isBookmarked;

    @Builder
    public MateBookmark(Mate mate, Member member) {
        this.mate = mate;
        this.member = member;
    }

    public void switchBookmarkStatus() {
        this.isBookmarked = Boolean.TRUE;
        }
}
