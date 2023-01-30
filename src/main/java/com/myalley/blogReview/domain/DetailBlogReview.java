package com.myalley.blogReview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DetailBlogReview {
    private BlogReview blogReview;
    private boolean likesStatus;
    private boolean bookmarkStatus;
}
