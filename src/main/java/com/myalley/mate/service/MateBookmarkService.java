package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import com.myalley.mate.dto.response.BookmarkSimpleResponse;
import com.myalley.mate.repository.MateBookmarkRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
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
public class MateBookmarkService {
    private final MateBookmarkRepository bookmarkRepository;
    private final MateService mateService;
    private final MemberService memberService;

    public BookmarkSimpleResponse createBookmark(Long memberId, Long mateId) {
        Member member = memberService.validateMember(memberId);
        Mate mate = mateService.validateExistMate(mateId);
        Optional<MateBookmark> bookmark = bookmarkRepository.findByMateAndMember(mate, member);

        //본인이 작성한 글에 북마크 추가하려는 경우
        if (mate.getMember().equals(member)) {
            throw new CustomException(MateExceptionType.CANNOT_BOOKMARK_MY_POST);
        }

        if (bookmark.isPresent()) {
            removeBookmark(bookmark.get().getId());
            mateService.bookmarkCountDown(mateId);

            return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에서 삭제되었습니다.", false);
        }

        MateBookmark mateBookmark = MateBookmark.builder()
                .member(member)
                .mate(mate)
                .build();

        bookmarkRepository.save(mateBookmark);
        mateService.bookmarkCountUp(mateId);

        return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에 추가되었습니다.", true);
    }

    private void removeBookmark(Long bookmarkId) {
        validateExistBookmark(bookmarkId);
        bookmarkRepository.deleteById(bookmarkId);
    }

    //북마크 추가한 메이트글 목록 페이징 조회하기
    public Page<MateBookmark> findBookmarkedMatesByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("id").descending());
        Member member = memberService.validateMember(memberId);
        return bookmarkRepository.findAllByMember(member, pageRequest);
    }

    public void validateExistBookmark(Long bookmarkId) {
        bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
    }
}
