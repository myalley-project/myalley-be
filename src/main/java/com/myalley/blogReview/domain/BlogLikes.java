package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="blog_likes")
@Getter
@NoArgsConstructor
public class BlogLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="blog_id")
    private BlogReview blog;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

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
