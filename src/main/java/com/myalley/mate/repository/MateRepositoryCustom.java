package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateRepositoryCustom {
    Page<Mate> findMates(String status, String title, Pageable pageable);	// offset,limit 전달
}
