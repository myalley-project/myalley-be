package com.myalley.mate.repository;

import com.myalley.mate.dto.response.MyMateBookmarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateBookmarkRepositoryCustom {
    Page<MyMateBookmarkResponse> findMateBookmarkByMember(Long memberId, Pageable pageable);
}
