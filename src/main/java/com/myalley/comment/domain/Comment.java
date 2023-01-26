package com.myalley.comment.domain;

import com.myalley.common.domain.BaseTime;
import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    private boolean softRemoved = false;

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

    public boolean isSoftRemoved() {
        return softRemoved;
    }

    public void changePretendingToBeRemoved() {
        this.softRemoved = true;
    }

    public void deleteChild(Comment reply) {
        children.remove(reply);
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

    public boolean hasNoReply() {
        return children.isEmpty();
    }

    public void updateContent(Long commentId, String content) {
        this.id = commentId;
        this.message = content;
    }

}
