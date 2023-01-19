package com.myalley.mate.dto;

import com.myalley.exhibition.dto.response.PagingDto;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MatePageResponse<T> {
    private List<T> mates;
    private PagingDto pageInfo;

    public MatePageResponse(List<T> mates, Page page) {
        this.mates = mates;
        this.pageInfo = new PagingDto(page.getNumber() +1,
                page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }
}
