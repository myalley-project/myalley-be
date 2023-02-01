package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exception.MemberExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionDetailResponse;
import com.myalley.exhibition.exhibitionImage.service.ImageService;
import com.myalley.exhibition.repository.ExhibitionBookmarkRepository;
import com.myalley.exhibition.repository.ExhibitionRepository;
import com.myalley.exhibition_deleted.domain.ExhibitionDeleted;
import com.myalley.exhibition_deleted.repository.ExhibitionDeletedRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ImageService imageService;
    private final ExhibitionDeletedRepository deletedRepository;
    private final MemberRepository memberRepository;
    private final ExhibitionBookmarkRepository bookmarkRepository;

    public ExhibitionBasicResponse save(ExhibitionRequest request) {

        Exhibition newExhibition = Exhibition.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .type(request.getType())
                .space(request.getSpace())
                .adultPrice(request.getAdultPrice())
                .fileName(request.getFileName())
                .posterUrl(request.getPosterUrl())
                .duration(request.getDuration())
                .webLink(request.getWebLink())
                .content(request.getContent())
                .author(request.getAuthor())
                .viewCount(0)
                .bookmarkCount(0)
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
    @Transactional
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
                .duration(exhibition.getDuration())
                .webLink(exhibition.getWebLink())
                .content(exhibition.getContent())
                .author(exhibition.getAuthor())
                .viewCount(exhibition.getViewCount())
                .bookmarkCount(0)
                .createdAt(exhibition.getCreatedAt())
                .deletedAt(exhibition.getModifiedAt())
                .build();
        deletedRepository.save(deleted);
        bookmarkRepository.deleteByExhibition(exhibition); //북마크 쪽에서 먼저 북마크 삭제해줌
        exhibitionRepository.deleteById(id);
    }

    //특정 전시글 상세페이지 조회 - 비회원
    public ExhibitionDetailResponse findInfoGeneral(Long id) {
        return exhibitionRepository.findById(id)
                .map(ExhibitionDetailResponse::of)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    //특정 전시글 상세페이지 조회 - 로그인 한 유저의 요청
    public ExhibitionDetailResponse findInfoMember(Long id, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));

        boolean bookmarked = bookmarkRepository.existsByExhibitionAndMember(exhibition, member);

        return ExhibitionDetailResponse.of(exhibition, bookmarked);

    }

    //조회수 증가
    @Transactional
    public void updateViewCount(Long id) {
        exhibitionRepository.updateViewCount(id);
    }

//    //전시회 상태와 유형 같이 검색
//    public Page<Exhibition> readPageAllSearch(String status, String type, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("createdAt").descending());
//        return exhibitionRepository.findByTypeOrStatus(type, status, pageRequest);
//
//    }

    //전시회 상태와 유형 같이 검색
    public Page<Exhibition> findStatusAndType(String status, String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("createdAt").descending());
        return exhibitionRepository.findByTypeContainingAndStatusContaining(type, status, pageRequest);

    }

    //전시회 관람여부만으로 목록 조회
    public Page<Exhibition> readPageAll(String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        return exhibitionRepository.findByStatusContaining(status, pageRequest);

    }

    //전시글 목록 조회 검색바 (status&title)
    public Page<Exhibition> findTitle(String status, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by("id").descending());
        return exhibitionRepository.findByStatusContainingAndTitleContaining(status, keyword, pageRequest);
    }

    //전시글 존재여부 확인
    public Exhibition verifyExhibition(Long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    @Transactional
    public void bookmarkCountUp(Long exhibitionId) {
        Optional<Exhibition> byId = exhibitionRepository.findById(exhibitionId);
        Exhibition exhibition = byId.get();
        exhibition.bookmarkCountUp();
    }

    @Transactional
    public void bookmarkCountDown(Long exhibitionId) {
        Optional<Exhibition> byId = exhibitionRepository.findById(exhibitionId);
        Exhibition exhibition = byId.get();
        exhibition.bookmarkCountDown();
    }
}
