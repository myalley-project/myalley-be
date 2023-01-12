package com.myalley.blogReview.domain;

import javax.persistence.*;

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
    private Long blogId;
    private String url;

    @Builder
    public BlogImage(String fileName, Long blogId, String url){
        this.fileName = fileName;
        this.blogId = blogId;
        this.url = url;
    }
}
