package com.myalley.blogReview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DetailBlogReview {
    private BlogReview blogReview;
    private boolean likesStatus;
    private boolean bookmarkStatus;
}
