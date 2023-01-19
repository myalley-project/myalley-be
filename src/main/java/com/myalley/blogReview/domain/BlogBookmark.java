package com.myalley.blogReview.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class BlogBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_bookmarks_id")
    private Long id;
    private Long memberId;
    private Long blogId;
    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public BlogBookmark(Long memberId, Long blogId){
        this.memberId = memberId;
        this.blogId = blogId;
    }

    //눌렀을 때 사용하는 칭기
    public void changeBookmarksStatus(){
        if(isDeleted.equals(Boolean.FALSE))
            this.isDeleted=Boolean.TRUE;
        else this.isDeleted=Boolean.FALSE;
    }
}
