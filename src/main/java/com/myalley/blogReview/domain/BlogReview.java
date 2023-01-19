package com.myalley.blogReview.domain;

import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.exception.CustomException;
import com.myalley.member.domain.Member;
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
    private String title;
    @Column(nullable = false)
    private String content;
    private Integer likeCount;
    private Integer viewCount;
    private Integer bookmarkCount;

    private String transportation;
    private String revisit;
    private String congestion;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "exhibition_id")
    private Long exhibition;

    @OneToMany(mappedBy = "blog")
    private List<BlogImage> images = new ArrayList<>();

    @Builder
    public BlogReview(String title, String content, LocalDate viewDate, String transportation,
                      String revisit, String congestion, Integer viewCount, Integer likeCount,
                      Integer bookmarkCount, Member member, Long exhibition){ //TransportationType transportation, RevisitType revisit, CongestionType congestion,
        this.title = title;
        this.content = content;
        this.viewDate = viewDate;
        this.transportation = transportation;
        this.revisit = revisit;
        this.congestion = congestion;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.bookmarkCount = bookmarkCount;
        this.member = member;
        this.exhibition = exhibition;
    }

    public void updateReview(BlogRequestDto newBlogReview){
        this.title= newBlogReview.getTitle();
        this.content = newBlogReview.getContent();
        this.viewDate = LocalDate.parse(newBlogReview.getViewDate());
        this.congestion = newBlogReview.getCongestion();
        this.revisit = newBlogReview.getRevisit();
        this.transportation = newBlogReview.getTransportation();
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
    //public void updateBookmarkCount(){ this.bookmarkCount++; }

    public void updateImages(BlogImage image){
        this.images.add(image);
    }

    public void setImage(BlogImage image){
            image.setBlog(this);
            this.images.add(image);
    }
}
