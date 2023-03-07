package com.myalley.exhibition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagingDto {
    private int page;
    private int size;
    private long totalElement;
    private int totalPage;

}
