package com.myalley.blogReview.domain;

import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="blog_likes")
@Getter
@NoArgsConstructor
@Where(clause="is_deleted = 0")
public class BlogLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="blog_id", nullable = false)
    private BlogReview blog;


    @Builder
    public BlogLikes(Member member, BlogReview blog){
        this.member = member;
        this.blog = blog;
    }

    public void changeLikesStatus() {
        if(isDeleted == null || isDeleted.equals(Boolean.TRUE)) {
            this.isDeleted=Boolean.FALSE;
            this.blog.increaseLikesCount();
        }
        else {
            this.isDeleted = Boolean.TRUE;
            this.blog.decreaseLikesCount();
        }
        this.updatedAt=LocalDateTime.now();
    }
}
