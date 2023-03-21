package com.myalley.mate.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MatePageResponse<T> {
    private List<T> mates;
    private int totalPage;
    private int totalElement;

    public MatePageResponse(List<T> mates, Page page) {
        this.mates = mates;
        this.totalPage = page.getTotalPages();
        this.totalElement = (int) page.getTotalElements();
    }
}
