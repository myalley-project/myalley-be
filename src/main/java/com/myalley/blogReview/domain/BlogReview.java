package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.test_user.TestMember;
import com.myalley.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class BlogReview extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long id;
    @Column(nullable = false)
    private LocalDate viewDate;
    @Column(nullable = false)
    private String time;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private Integer likeCount = 0;
    private Integer viewCount = 0;
    private Integer bookmarkCount = 0;

    private String transportation;
    private String revisit;
    private String congestion;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private TestMember testMember;
    @Column(name = "exhibition_id")
    private Long exhibition;

    @OneToMany(mappedBy = "blog")
    private List<BlogImage> images = new ArrayList<>();

    @Builder
    public BlogReview(String title, String content, LocalDate viewDate, String time, String transportation,
                      String revisit, String congestion, TestMember testMember, Long exhibition){
        this.title = title;
        this.content = content;
        this.viewDate = viewDate;
        this.time = time;
        this.transportation = transportation;
        this.revisit = revisit;
        this.congestion = congestion;
        this.testMember = testMember;
        this.exhibition = exhibition;
    }

    public void updateReview(BlogReview target){
        this.title= target.getTitle();
        this.content = target.getContent();
        this.viewDate = target.getViewDate();
        this.time = target.getTime();
        this.congestion = target.getCongestion();
        this.revisit = target.getRevisit();
        this.transportation = target.getTransportation();
    }

    public void updateViewCount(){
        this.viewCount++;
    }
    public void updateLikeCount(Boolean status){
        if(status.equals(Boolean.FALSE)) 
            this.likeCount++;
        else if(status.equals(Boolean.TRUE))
            this.likeCount--;
        else //음.. 나중에 바꾸기
            throw new CustomException(BlogReviewExceptionType.LIKES_BAD_REQUEST);
    }
    //public void updateBookmarkCount(Boolean status){
    // if(status.equals(Boolean.FALSE)) this.bookmarkCount++;
    // else this.bookmarkCount--; }

    //public void updateImages(BlogImage image){
    //    this.images.add(image);
    //}

    public void setImage(BlogImage image){
            this.images.add(image);
    }
    public void setMember(TestMember testMember){
        this.testMember = testMember;
    }

}
