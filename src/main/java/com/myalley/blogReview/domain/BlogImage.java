package com.myalley.blogReview.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="blog_image")
@Getter
@NoArgsConstructor
public class BlogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_image_id")
    private Long id;
    private String fileName;
    private String url;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "blog_id", nullable = false)
    private BlogReview blog;

    @Builder
    public BlogImage(String fileName, String url){
        this.fileName = fileName;
        this.url = url;
    }

    public void setBlog(BlogReview blog){
        blog.setImage(this);
        this.blog=blog;
    }
}
