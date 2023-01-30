package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exception.MemberExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import com.myalley.mate.dto.BookmarkSimpleResponse;
import com.myalley.mate.dto.MateMyResponse;
import com.myalley.mate.repository.MateBookmarkRepository;
import com.myalley.mate.repository.MateRepository;
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
public class MateBookmarkService {
    private final MateBookmarkRepository bookmarkRepository;
    private final MateRepository mateRepository;
    private final MemberRepository memberRepository;
    private final MateService mateService;

    public BookmarkSimpleResponse addNewBookmark(Long memberId, Long mateId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        Mate mate = mateRepository.findById(mateId)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));

        Optional<MateBookmark> bookmark = bookmarkRepository.findByMateAndMember(mate, member);

        //본인이 작성한 글에 북마크 추가하려는 경우
        if (mate.getMember().equals(member)) {
            throw new CustomException(MateExceptionType.CANNOT_BOOKMARK_MY_POST);
        }

        if (bookmark.isPresent()) {
            deleteBookmark(bookmark.get().getId());
            mateService.bookmarkCountDown(mateId);

            return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에서 삭제되었습니다.", false);
        }

        MateBookmark mateBookmark = MateBookmark.builder()
                .member(member)
                .mate(mate)
                .build();

        mateBookmark.addBookmark();
        bookmarkRepository.save(mateBookmark);
        mateService.bookmarkCountUp(mateId);

        return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에 추가되었습니다.", true);
    }

    private void deleteBookmark(Long bookmarkId) {
        MateBookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
        bookmarkRepository.deleteById(bookmarkId);
    }

    //북마크 추가한 메이트글 목록 페이징 조회하기
    public Page<MateBookmark> findBookmarkedMate(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));
        return bookmarkRepository.findAllByMember(member, pageRequest);
    }

    public MateMyResponse findMateInfo(Long mateId) {
        return mateRepository.findById(mateId)
                .map(MateMyResponse::of)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
    }
}
