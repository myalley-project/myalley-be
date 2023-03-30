package com.myalley.mate.repository;

import com.myalley.mate.dto.response.MateMyResponse;
import com.myalley.mate.dto.response.MateSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateRepositoryCustom {
    Page<MateSimpleResponse> findMatesByStatusAndTitle(String status, String title, Pageable pageable);	// offset,limit 전달
    Page<MateSimpleResponse> findMatesByExhibitionId(Long exhibitionId, Pageable pageable);
    Page<MateMyResponse> findMatesByMember(Long memberId, Pageable pageable);
}
