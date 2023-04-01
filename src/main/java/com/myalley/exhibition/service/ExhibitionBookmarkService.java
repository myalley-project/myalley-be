package com.myalley.exhibition.service;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import com.myalley.exhibition.dto.response.BookmarkResponseDto;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.repository.ExhibitionBookmarkRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitionBookmarkService {

    private final ExhibitionService exhibitionService;
    private final ExhibitionBookmarkRepository bookmarkRepository;
    private final MemberService memberService;

    @Transactional
    public BookmarkResponseDto switchExhibitionBookmark(Member member, Long exhibitionId) {

        Exhibition exhibition = exhibitionService.validateExistExhibition(exhibitionId);
        ExhibitionBookmark bookmark = findExhibitionBookmark(exhibition, member);

        if (bookmark.isBookmarked()) {
            bookmarkRepository.deleteById(bookmark.getId());
            exhibitionService.bookmarkCountDown(exhibitionId);
            return new BookmarkResponseDto("전시글 북마크 목록에서 삭제되었습니다.", false);
        }

        bookmark.switchBookmarkStatus();
        exhibitionService.bookmarkCountUp(exhibitionId);
        bookmarkRepository.save(bookmark);
        return new BookmarkResponseDto("전시글 북마크 목록에 추가되었습니다.", true);
    }

    public Page<ExhibitionBasicResponse> findBookmarksByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 8);
        return bookmarkRepository.findExhibitionBookmarkByMember(memberId, pageRequest);
    }

    public ExhibitionBookmark findExhibitionBookmark(Exhibition exhibition, Member member) {
        return bookmarkRepository.findByExhibitionAndMember(exhibition, member)
                .orElseGet(() -> ExhibitionBookmark.builder()
                        .member(member)
                        .exhibition(exhibition)
                        .build());
    }
}
