package com.myalley.blogReview.domain;

import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.option.TransportationType;
import com.myalley.blogReview.option.RevisitType;
import com.myalley.blogReview.option.CongestionType;
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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransportationType transportation;  //주차공간
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RevisitType revisit;  //재방문의향
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CongestionType congestion; //혼잡도

    @Column(name = "member_id")
    private Long member;
    @Column(name = "exhibition_id")
    private Long exhibition;

    @OneToMany(mappedBy = "blog")
    private List<BlogImage> images = new ArrayList<>();

    @Builder
    public BlogReview(String title, String content, LocalDate viewDate, TransportationType transportation,
                      RevisitType revisit, CongestionType congestion, Integer viewCount, Integer likeCount,
                      Long member, Long exhibition){
        this.title = title;
        this.content = content;
        this.viewDate = viewDate;
        this.transportation = transportation;
        this.revisit = revisit;
        this.congestion = congestion;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
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
    public void updateLikeCount(){
        this.likeCount++;
    }

    public void updateImages(BlogImage image){
        this.images.add(image);
    }

    public void setImages(List<BlogImage> images){
        for(BlogImage image : images){
            image.setBlog(this);
            this.images.add(image);
        }
    }
    public void setImage(BlogImage image){
            image.setBlog(this);
            this.images.add(image);
    }
}
