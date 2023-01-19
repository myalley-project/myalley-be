package com.myalley.blogReview.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BlogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_image_id")
    private Long id;
    private String fileName;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private BlogReview blog;
    private String url;

    @Builder
    public BlogImage(String fileName, String url){
        this.fileName = fileName;
        this.url = url;
    }

    public void setBlog(BlogReview blog){
        //blog.updateImages(this);
        this.blog=blog;
    }
}
