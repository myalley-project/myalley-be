package com.myalley.mate.service;

import com.myalley.exhibition.service.ExhibitionService;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
