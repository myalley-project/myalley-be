package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.response.MateDetailResponse;
import com.myalley.mate.dto.request.MateRequest;
import com.myalley.mate.dto.response.MateMyResponse;
import com.myalley.mate.dto.response.MateSimpleResponse;
import com.myalley.mate.dto.request.MateUpdateRequest;
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

    public void createMate(MateRequest request, Long memberId) {
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

        mateRepository.save(newMate);
    }

    @Transactional
    public void updateMate(Long mateId, MateUpdateRequest request, Long memberId) {
        Mate findMate = validateExistMate(mateId);

        if (!memberId.equals(findMate.getMember().getMemberId())) {
                throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }

        findMate.updateInfo(mateId, request);
        findMate.updateExhibition(exhibitionService.validateExistExhibition(request.getExhibitionId()));
    }

    @Transactional
    public void updateViewCount(Long mateId) {
        mateRepository.updateViewCount(mateId);
    }

    public MateDetailResponse findByMateIdAndMemberId(Long mateId, Long memberId) {
//        validateMateDeletedOrNot(mateId);
        if (memberId == 0) {
            return mateRepository.findById(mateId)
                    .map(MateDetailResponse::of)
                    .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
        }

        Member member = memberService.validateMember(memberId);
        Mate mate = validateExistMate(mateId);
        boolean bookmarked = bookmarkRepository.existsByMateAndMember(mate, member);

        return MateDetailResponse.of(mate, bookmarked);
    }

    @Transactional
    public void removeByMateIdAndMemberId(Long mateId, Long memberId) {
        Mate mate = validateExistMate(mateId);

        if (!memberId.equals(mate.getMember().getMemberId())) {
            throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }
        mateRepository.deleteById(mateId);
    }

    public Page<MateMyResponse> findMyMates(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("id").descending());
        memberService.validateMember(memberId);
        return mateRepository.findMatesByMember(memberId, pageRequest);
    }

    public Page<MateSimpleResponse> findMatesByExhibitionId(Long exhibitionId, int page) {
        PageRequest pageRequest = PageRequest.of(page -1, 4, Sort.by("createdAt").descending());
        exhibitionService.validateExistExhibition(exhibitionId);

        return mateRepository.findMatesByExhibitionId(exhibitionId, pageRequest);
    }

    public Page<MateSimpleResponse> findMatesByStatusAndTitle(String status, String title, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 4, Sort.by("id").descending());
        return mateRepository.findMatesByStatusAndTitle(status, title, pageRequest);
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

//    public Mate validateMateDeletedOrNot(Long mateId) {
//        return mateRepository.findByIdAndIsDeleted(mateId)
//                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
//    }
}
