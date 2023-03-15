package com.myalley.mate.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MatePageResponse<T> {
    private List<T> mates;
    private int totalPage;

    public MatePageResponse(List<T> mates, Page page) {
        this.mates = mates;
        this.totalPage = page.getTotalPages();
    }
}
