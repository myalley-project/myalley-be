package com.myalley.mate.domain;

import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Table(name = "mate_bookmark")
@SQLDelete(sql = "UPDATE mate_bookmark SET isBookmarked = false WHERE mate_book_id = ?")
@Where(clause = "isBookmarked = true")
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

    private boolean isBookmarked = Boolean.TRUE;

    @Builder
    public MateBookmark(Mate mate, Member member) {
        this.mate = mate;
        this.member = member;
    }

}
