package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.common.domain.BaseTime;
import com.myalley.test_user.TestMember;
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
    private TestMember testMember;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="blog_id")
    private BlogReview blog;
    private Boolean isDeleted=Boolean.TRUE;

    @Builder
    public Likes(TestMember testMember, BlogReview blog){
        this.testMember = testMember;
        this.blog=blog;
    }

    //눌렀을 때 사용하는 칭기
    public void changeLikesStatus(){
        if(isDeleted.equals(Boolean.FALSE))
            this.isDeleted=Boolean.TRUE;
        else this.isDeleted=Boolean.FALSE;
        this.blog.updateLikeCount(this.isDeleted);
    }

}
