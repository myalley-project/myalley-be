package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exception.MemberExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import com.myalley.exhibition.dto.response.BookmarkResponseDto;
import com.myalley.exhibition.repository.ExhibitionBookmarkRepository;
import com.myalley.exhibition.repository.ExhibitionRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final ExhibitionService exhibitionService;
    private final ExhibitionBookmarkRepository bookmarkRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final MemberRepository memberRepository;

    public BookmarkResponseDto addNewBookmark(Long memberId, Long exhibitionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));

         Optional<ExhibitionBookmark> bookmark = bookmarkRepository.findByExhibitionAndMember(exhibition, member);

        if (bookmark.isPresent()) {
            deleteBookmark(bookmark.get().getId());
            exhibitionService.bookmarkCountDown(exhibitionId);

            return new BookmarkResponseDto("전시글 북마크 목록에서 삭제되었습니다.", false);
        }

        ExhibitionBookmark exBookmark = ExhibitionBookmark.builder()
                .member(member)
                .exhibition(exhibition)
                .build();

        exBookmark.addBookmark();
        bookmarkRepository.save(exBookmark);
        exhibitionService.bookmarkCountUp(exhibitionId);

        return new BookmarkResponseDto("전시글 북마크 목록에 추가되었습니다.", true);
    }

    private void deleteBookmark(Long bookmarkId) {
        ExhibitionBookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_BOOKMARK_NOT_FOUND));
        bookmarkRepository.deleteById(bookmarkId);
    }

    //북마크 추가한 전시글 목록 페이징 조회하기
    public Page<ExhibitionBookmark> findBookmarkedExhibitions(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));
        return bookmarkRepository.findAllByMember(member, pageRequest);
    }
}
