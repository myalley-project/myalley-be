package com.myalley.blogReview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myalley.member.domain.Member;
import com.myalley.test_user.TestMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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
    //private Member member;
    private TestMember member;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "blog_id")
    private BlogReview blog;
    private LocalDateTime createdAt;

    @Builder
    public BlogBookmark(TestMember member, BlogReview blog, LocalDateTime createdAt){
        this.member = member;
        this.blog = blog;
        this.createdAt = createdAt;
    }
}
