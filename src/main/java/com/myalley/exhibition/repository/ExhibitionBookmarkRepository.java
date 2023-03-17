package com.myalley.exhibition.repository;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import com.myalley.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExhibitionBookmarkRepository extends JpaRepository<ExhibitionBookmark, Long> {
    Optional<ExhibitionBookmark> findByExhibitionAndMember(Exhibition exhibition, Member member);
    boolean existsByExhibitionAndMember(Exhibition exhibition, Member member);
    Page<ExhibitionBookmark> findAllByMember(Member member, PageRequest pageRequest);
}
