package com.myalley.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class pagingDto {
    private int page;
    private int size;
    private long totalElement;
    private int totalPage;
}
