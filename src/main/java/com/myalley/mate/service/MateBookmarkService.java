package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import com.myalley.mate.dto.response.BookmarkSimpleResponse;
import com.myalley.mate.dto.response.MyMateBookmarkResponse;
import com.myalley.mate.repository.MateBookmarkRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateBookmarkService {
    private final MateBookmarkRepository bookmarkRepository;
    private final MateService mateService;
    private final MemberService memberService;

    @Transactional
    public BookmarkSimpleResponse switchMateBookmark(Member member, Long mateId) {
        Mate mate = mateService.validateMateDeletedOrNot(mateId);
        MateBookmark bookmark = findMateBookmark(mate, member);
        validateAuthor(mate, member);

        if (bookmark.isBookmarked()) {
            bookmarkRepository.deleteById(bookmark.getId());
            mateService.bookmarkCountDown(mateId);
            return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에서 삭제되었습니다.", false);
        }

        bookmark.switchBookmarkStatus();
        mateService.bookmarkCountUp(mateId);
        bookmarkRepository.save(bookmark);
        return new BookmarkSimpleResponse("메이트 모집글이 북마크 목록에 추가되었습니다.", true);
    }

    public void validateAuthor(Mate mate, Member member) {
        if (mate.getMember().equals(member)) {
            throw new CustomException(MateExceptionType.CANNOT_BOOKMARK_MY_POST);
        }
    }

    public Page<MyMateBookmarkResponse> findBookmarkedMatesByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("mate_id").descending());
        return bookmarkRepository.findMateBookmarkByMember(memberId, pageRequest);
    }

    public MateBookmark findMateBookmark(Mate mate, Member member) {
        return bookmarkRepository.findByMateAndMember(mate, member)
                .orElseGet(() -> MateBookmark.builder()
                        .member(member)
                        .mate(mate)
                        .build());
    }
}
