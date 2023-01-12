package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionDetailResponse;
import com.myalley.exhibition.exhibitionImage.service.ImageService;
import com.myalley.exhibition.options.DeletionStatus;
import com.myalley.exhibition.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ImageService imageService;

    /**
     * 전시글 삭제 요청
     * @param request 전시회 정보를 담은 json body - title,status,type,space,fileName,poesterUrl,date,webLink,content,author 정보를 보내준다.
     * @return 방금 등록한 전시회 정보의 id,title,startDate,endDate,viewCount,posterUrl 를 전달한다.
     * @author Hwadam
     * */

    public ExhibitionBasicResponse save(ExhibitionRequest request) {

        Exhibition newExhibition = Exhibition.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .type(request.getType())
                .space(request.getSpace())
                .adultPrice(request.getAdultPrice())
                .fileName(request.getFileName())
                .posterUrl(request.getPosterUrl())
                .date(request.getDate())
                .webLink(request.getWebLink())
                .content(request.getContent())
                .author(request.getAuthor())
                .isDeleted(DeletionStatus.NOT_DELETED)
                .viewCount(0)
                .build();

        return ExhibitionBasicResponse.of(exhibitionRepository.save(newExhibition));
    }

    //전시글 수정
    @Transactional
    public ExhibitionBasicResponse update(ExhibitionUpdateRequest request, Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
        if (!request.getFileName().equals(exhibitionRepository.getById(id).getFileName())) {
            updatePoster(id);
            exhibition.updateInfo(id, request);
        } else {
            exhibition.updateInfo(id, request);
        }
       return ExhibitionBasicResponse.of(exhibition);
    }

    public void updatePoster(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
        String target = exhibition.getFileName();
        imageService.removeFile(target);
    }

    //전시글 삭제 - 상태변화 시키기
    public ExhibitionBasicResponse delete(Long id) {
       return exhibitionRepository.findById(id)
               .map(exhibition -> {
                   exhibition.changeDeletion();
                   return exhibitionRepository.save(exhibition);
               })
               .map(ExhibitionBasicResponse::of)
               .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    //조회수 증가
    @Transactional
    public void updateViewCount(Long id) {
        exhibitionRepository.updateViewCount(id);
    }

    //특정 전시글 상세페이지 조회
    public ExhibitionDetailResponse findInfo(Long id) {
        return exhibitionRepository.findById(id)
                .map(ExhibitionDetailResponse::of)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    //전시글 전체목록 조회

}
