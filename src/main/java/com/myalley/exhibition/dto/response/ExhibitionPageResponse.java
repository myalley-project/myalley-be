package com.myalley.exhibition.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ExhibitionPageResponse<T> {
    private List<T> exhibitions;
    private int totalPage;

    public ExhibitionPageResponse(List<T> exhibition, Page page) {
        this.exhibitions = exhibition;
        this.totalPage = page.getTotalPages();
    }

}
