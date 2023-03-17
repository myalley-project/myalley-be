package com.myalley.comment.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.mate.domain.Mate;
import com.myalley.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET isDeleted = true WHERE comment_id = ?")
//@Where(clause = "isDeleted = false")
@Entity
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mateId")
    private Mate mate;

    @Column(nullable = false)
    private String message;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Comment(Member member, Mate mate, String message, Comment parent) {
        this.member = member;
        this.mate = mate;
        this.message = message;
        this.parent = parent;
    }

    public static Comment parent(Member member, Mate mate, String message) {
        return new Comment(member, mate, message, null);
    }

    public static Comment child(Member member, Mate mate,String message,Comment parent) {
        Comment child = new Comment(member, mate, message, parent);
        parent.getChildren().add(child);
        return child;
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

}
