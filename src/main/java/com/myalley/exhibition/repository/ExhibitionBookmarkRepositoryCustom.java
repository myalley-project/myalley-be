package com.myalley.exhibition.repository;

import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExhibitionBookmarkRepositoryCustom {
    Page<ExhibitionBasicResponse> findExhibitionBookmarkByMember(Long memberId, Pageable pageable);
}
