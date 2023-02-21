package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="blog_bookmark")
@Getter
@NoArgsConstructor
public class BlogBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_bookmarks_id")
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "blog_id")
    private BlogReview blog;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    @Builder
    public BlogBookmark(Member member, BlogReview blog){
        this.member = member;
        this.blog = blog;
    }

    public void changeBookmarkStatus(){
        if(isDeleted == null || isDeleted.equals(Boolean.TRUE)){
            this.isDeleted = Boolean.FALSE;
            this.blog.increaseBookmarkCount();
        }
        else {
            this.isDeleted = Boolean.TRUE;
            this.blog.decreaseBookmarkCount();
        }
        this.updatedAt = LocalDateTime.now();
    }
}
