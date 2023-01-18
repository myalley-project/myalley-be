package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionSearchRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionDetailResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.exhibition.exhibitionImage.service.ImageService;
import com.myalley.exhibition.mapper.ExhibitionMapper;
import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.repository.ExhibitionRepository;
import com.myalley.exhibition_deleted.domain.ExhibitionDeleted;
import com.myalley.exhibition_deleted.repository.ExhibitionDeletedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ImageService imageService;
    private final ExhibitionDeletedRepository deletedRepository;
    private final ExhibitionMapper mapper;

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

    //포스터 이미지 수정
    public void updatePoster(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
        String target = exhibition.getFileName();
        imageService.removeFile(target);
    }

    //전시글 삭제 - 삭제 테이블로 이동시키고 기존 테이블에서 삭제
    public void delete(Long id) {
       Exhibition exhibition = exhibitionRepository.findById(id)
               .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));

        String target = exhibition.getFileName();
        imageService.removeFile(target);

        ExhibitionDeleted deleted = ExhibitionDeleted.builder()
                .title(exhibition.getTitle())
                .status(exhibition.getStatus())
                .type(exhibition.getType())
                .space(exhibition.getSpace())
                .adultPrice(exhibition.getAdultPrice())
                .fileName("")
                .posterUrl("")
                .date(exhibition.getDate())
                .webLink(exhibition.getWebLink())
                .content(exhibition.getContent())
                .author(exhibition.getAuthor())
                .viewCount(exhibition.getViewCount())
                .createdAt(exhibition.getCreatedAt())
                .deletedAt(exhibition.getModifiedAt())
                .build();
        deletedRepository.save(deleted);
        exhibitionRepository.deleteById(id);
    }

    //특정 전시글 상세페이지 조회
    public ExhibitionDetailResponse findInfo(Long id) {
        return exhibitionRepository.findById(id)
                .map(ExhibitionDetailResponse::of)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    //조회수 증가
    @Transactional
    public void updateViewCount(Long id) {
        exhibitionRepository.updateViewCount(id);
    }
//    Page<Postscript> findAllByPostscriptStatus(Pageable pageable, Postscript.PostscriptStatus postscriptStatus);
//
//    List<Postscript> findAllByMemberAndPostscriptStatus(Member member, Postscript.PostscriptStatus postscriptStatus);
    //전시글 목록 조회
//    @Transactional(readOnly = true)
//    public List<ExhibitionBasicResponse> findAll(Page<Exhibition> ) {
//        return exhibitionRepository.findAllByStatus(status, pageable)
//                .stream()
//                .map(ExhibitionBasicResponse::of)
//                .collect(Collectors.toList());
//    }

//    public ExhibitionPageResponse<ExhibitionBasicResponse> findBySearchCondition(final ExhibitionSearchRequest searchRequest,
//                                                        final Pageable pageable) {
//        final Page<Exhibition> page = findBySearchConditions(searchRequest, pageable);
//        final ExhibitionStatus status =
//
//        }
//    }
//
//
//    public Page<Exhibition> findBySearchConditions(final ExhibitionSearchRequest searchRequest,
//                                                   final Pageable pageable) {
//
//    }

    //전시회 상태와 유형 같이 검색
    public Page<Exhibition> readPageAllSearch(String status, String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        return exhibitionRepository.findByStatusOrType(
                                status,
                                type,
                                pageRequest);

    }

    //전시회 관람여부만으로 목록 조회
    public Page<Exhibition> readPageAll(String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        return exhibitionRepository.findAllByStatus(status, pageRequest);

    }

    //전시글 존재여부 확인
    public Exhibition verifyExhibition(Long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }
}
