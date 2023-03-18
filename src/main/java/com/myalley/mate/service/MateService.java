package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MateDetailResponse;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.repository.MateBookmarkRepository;
import com.myalley.mate.repository.MateRepository;
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
public class MateService {

    private final MateRepository mateRepository;
    private final ExhibitionService exhibitionService;
    private final MemberService memberService;
    private final MateBookmarkRepository bookmarkRepository;

    //메이트글 등록
    public MateSimpleResponse createMate(MateRequest request, Long memberId) {
        Mate newMate = Mate.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .mateGender(request.getMateGender())
                .mateAge(request.getMateAge())
                .availableDate(request.getAvailableDate())
                .content(request.getContent())
                .contact(request.getContact())
                .viewCount(0)
                .bookmarkCount(0)
                .exhibition(exhibitionService.validateExistExhibition(request.getExhibitionId()))
                .member(memberService.validateMember(memberId))
                .build();
        return MateSimpleResponse.of(mateRepository.save(newMate));
    }

    //메이트글 수정
    @Transactional
    public MateSimpleResponse updateMate(Long mateId, MateUpdateRequest request, Long memberId) {
        Mate findMate = validateExistMate(mateId);

        if (!memberId.equals(findMate.getMember().getMemberId())) {
                throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }

        findMate.updateInfo(mateId, request);
        findMate.updateExhibition(exhibitionService.validateExistExhibition(request.getExhibitionId()));
        return MateSimpleResponse.of(findMate);
    }

    //메이트글 조회수 증가
    @Transactional
    public void updateViewCount(Long mateId) {
        mateRepository.updateViewCount(mateId);
    }

    //메이트글 상세조회 - 비회원
    public MateDetailResponse findByMateId(Long mateId) {
        return mateRepository.findById(mateId)
                .map(MateDetailResponse::of)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
    }

    //메이트글 상세조회 - 로그인 한 회원의 요청
    public MateDetailResponse findByMateIdAndMemberId(Long mateId, Long memberId) {
        Member member = memberService.verifyMember(memberId);
        Mate mate = validateExistMate(mateId);
        boolean bookmarked = bookmarkRepository.existsByMateAndMember(mate, member);

        return MateDetailResponse.of(mate, bookmarked);
    }

    //메이트글 삭제
    @Transactional
    public void removeByMateIdAndMemberId(Long mateId, Long memberId) {
        Mate mate = validateExistMate(mateId);

        if (!memberId.equals(mate.getMember().getMemberId())) {
            throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }

        bookmarkRepository.deleteByMate(mate); //북마크 쪽에서 먼저 북마크 삭제해줌
        mateRepository.deleteById(mateId);
    }

    //본인이 작성한 메이트글 조회
    public Page<Mate> findMyMates(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("createdAt").descending());
        Member member = memberService.verifyMember(memberId);
        return mateRepository.findByMember(member, pageRequest);
    }

    //전시글 상세페이지에서 해당 전시회 id에 해당하는 메이트글 목록조회
    public Page<Mate> findMatesByExhibitionId(Long exhibitionId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("createdAt").descending());
        Exhibition exhibition = exhibitionService.validateExistExhibition(exhibitionId);

        return mateRepository.findByExhibition(exhibition, pageRequest);
    }

    public Page<Mate> findMatesByStatusAndTitle(String status, String title, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 4, Sort.by("id").descending());
        return mateRepository.findMates(status, title, pageRequest);
    }

    @Transactional
    public void bookmarkCountUp(Long mateId) {
        Mate mate = validateExistMate(mateId);
        mate.bookmarkCountUp();
    }

    @Transactional
    public void bookmarkCountDown(Long mateId) {
        Mate mate = validateExistMate(mateId);
        mate.bookmarkCountDown();
    }

    public Mate validateExistMate(Long mateId) {
        return mateRepository.findById(mateId)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
    }
}
