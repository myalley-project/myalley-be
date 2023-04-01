package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExhibitionRepositoryCustom {
    Page<ExhibitionBasicResponse> findPagedExhibitions(String status, String type, String title, Pageable pageable);	// offset,limit 전달

}
