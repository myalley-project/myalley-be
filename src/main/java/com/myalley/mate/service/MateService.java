package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MateDetailResponse;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.mate_deleted.MateDeleted;
import com.myalley.mate.mate_deleted.MateDeletedRepository;
import com.myalley.mate.repository.MateRepository;
import com.myalley.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MateService {

    private final MateRepository mateRepository;
    private final ExhibitionService exhibitionService;
    private final MateDeletedRepository deletedRepository;
    private final MemberService memberService;

    //메이트글 등록
    public MateSimpleResponse save(MateRequest request, Long memberId) {
        Mate newMate = Mate.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .mateGender(request.getMateGender())
                .mateAge(request.getMateAge())
                .availableDate(request.getAvailableDate())
                .content(request.getContent())
                .contact(request.getContact())
                .viewCount(0)
                .exhibition(exhibitionService.verifyExhibition(request.getExhibitionId()))
                .member(memberService.verifyMember(memberId))
                .build();
        return MateSimpleResponse.of(mateRepository.save(newMate));
    }

    //메이트글 수정
    @Transactional
    public MateSimpleResponse update(Long id, MateUpdateRequest request, Long memberId) {
        Mate findMate = mateRepository.findById(id)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));

        if (!memberId.equals(findMate.getMember().getMemberId())) {
                throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }

        findMate.updateInfo(id, request);
        findMate.updateExhibition(exhibitionService.verifyExhibition(request.getExhibitionId()));
        return MateSimpleResponse.of(findMate);
    }

    //메이트글 조회수 증가
    @Transactional
    public void updateViewCount(Long id) {
        mateRepository.updateViewCount(id);
    }

    //메이트글 상세조회
    public MateDetailResponse findDetail(Long id) {
        return mateRepository.findById(id)
                .map(MateDetailResponse::of)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));
    }

    //메이트글 삭제
    public void delete(Long id, Long memberId) {
        Mate mate = mateRepository.findById(id)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));

        if (!memberId.equals(mate.getMember().getMemberId())) {
            throw new CustomException(MateExceptionType.UNAUTHORIZED_ACCESS);
        }

        MateDeleted deleted = MateDeleted.builder()
                .title(mate.getTitle())
                .status(mate.getStatus())
                .mateGender(mate.getMateGender())
                .mateAge(mate.getMateAge())
                .availableDate(mate.getAvailableDate())
                .content(mate.getContent())
                .contact(mate.getContact())
                .viewCount(mate.getViewCount())
                .exhibition(exhibitionService.verifyExhibition(mate.getExhibition().getId()))
                .member(memberService.verifyMember(mate.getMember().getMemberId()))
                .build();
        deletedRepository.save(deleted);
        mateRepository.deleteById(id);
    }

    //메이트글 목록조회 (모집여부로 판단)
     public Page<Mate> readPageAll(String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        return mateRepository.findAllByStatus(status, pageRequest);
    }
}
