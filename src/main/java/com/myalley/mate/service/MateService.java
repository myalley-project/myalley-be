package com.myalley.mate.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MateDetailResponse;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MateService {

    private final MateRepository mateRepository;
    private final ExhibitionService exhibitionService;

    //메이트글 등록
    public MateSimpleResponse save(MateRequest request) {
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
                .build();
        return MateSimpleResponse.of(mateRepository.save(newMate));
    }

    //메이트글 수정
    @Transactional
    public MateSimpleResponse update( Long id, MateUpdateRequest request) {
        Mate findMate = mateRepository.findById(id)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));

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
}
