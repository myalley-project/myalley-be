package com.myalley.mate.repository;

import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import com.myalley.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateBookmarkRepository extends JpaRepository<MateBookmark, Long>, MateBookmarkRepositoryCustom {
    Optional<MateBookmark> findByMateAndMember(Mate mate, Member member);
    boolean existsByMateAndMember(Mate mate, Member member);
}
