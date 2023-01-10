package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionResponse;
import com.myalley.exhibition.exhibitionImage.service.ImageService;
import com.myalley.exhibition.options.DeletionStatus;
import com.myalley.exhibition.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ImageService imageService;

    //전시글 등록
    public ExhibitionResponse save(ExhibitionRequest request) {

        Exhibition newExhibition = Exhibition.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .type(request.getType())
                .space(request.getSpace())
                .adultPrice(request.getAdultPrice())
                .youthPrice(request.getYouthPrice())
                .kidPrice(request.getKidPrice())
                .fileName(request.getFileName())
                .posterUrl(request.getPosterUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .webLink(request.getWebLink())
                .content(request.getContent())
                .purpose(request.getPurpose())
                .author(request.getAuthor())
                .isDeleted(DeletionStatus.NOT_DELETED)
                .viewCount(0)
                .build();

        return ExhibitionResponse.of(exhibitionRepository.save(newExhibition));
    }

    //전시글 수정
    public ExhibitionResponse update(ExhibitionUpdateRequest request, Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
        exhibition.updateInfo(id, request); //도메인에 void 메소드로 추가해보기
       return ExhibitionResponse.of(exhibitionRepository.save(exhibition));


    }
}
