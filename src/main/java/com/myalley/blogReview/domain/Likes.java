package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.common.domain.BaseTime;
import com.myalley.user.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Likes extends BaseTime {
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
    private Boolean isDeleted;

    @Builder
    public Likes(Member member,BlogReview blog){
        this.member=member;
        this.blog=blog;
    }

    //눌렀을 때 사용하는 칭기
    public void changeLikesStatus(){
        if(isDeleted.equals(null))
            this.isDeleted=Boolean.FALSE;
        else if(isDeleted.equals(Boolean.FALSE))
            this.isDeleted=Boolean.TRUE;
        else this.isDeleted=Boolean.FALSE;
        this.blog.updateLikeCount(this.isDeleted);
    }

}
