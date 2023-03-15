package com.myalley.exhibition.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.ExhibitionExceptionType;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionDetailResponse;
import com.myalley.exhibition.repository.ExhibitionBookmarkRepository;
import com.myalley.exhibition.repository.ExhibitionRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
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
    private final ExhibitionImageService exhibitionImageService;
    private final MemberService memberService;
    private final ExhibitionBookmarkRepository bookmarkRepository;

    public String save(ExhibitionRequest request) {
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

        exhibitionRepository.save(newExhibition);
        return "전시글 등록이 완료되었습니다.";
    }

    @Transactional
    public String update(ExhibitionUpdateRequest request, Long id) {
        Exhibition exhibition = verifyExhibition(id);
        if (!checkFileChanged(request, id)) {
            updatePoster(id);
            exhibition.updateInfo(id, request);
        } else {
            exhibition.updateInfo(id, request);
        }
        return "전시글 정보 수정이 완료되었습니다.";
    }

    //포스터 이미지 변경됐는 지 확인
    public boolean checkFileChanged(ExhibitionUpdateRequest request, Long id) {
        return request.getFileName().equals(exhibitionRepository.getById(id).getFileName());
    }

    //포스터 이미지 수정
    public void updatePoster(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
        String target = exhibition.getFileName();
        exhibitionImageService.removeFile(target);
    }

    @Transactional
    public void delete(Long id) {
        Exhibition exhibition = verifyExhibition(id);
        exhibitionRepository.deleteById(id);
    }

    //특정 전시글 상세페이지 조회 - 비회원
    public ExhibitionDetailResponse findInfoGeneral(Long id) {
        return exhibitionRepository.findById(id)
                .map(ExhibitionDetailResponse::of)
                .orElseThrow(() -> new CustomException(ExhibitionExceptionType.EXHIBITION_NOT_FOUND));
    }

    //특정 전시글 상세페이지 조회 - 로그인 한 회원
    public ExhibitionDetailResponse findInfoMember(Long id, Long memberId) {
        Member member = memberService.verifyMember(memberId);
        Exhibition exhibition = verifyExhibition(id);

        return ExhibitionDetailResponse.of(exhibition, verifyBookmark(exhibition, member));
    }

    //북마크 추가여부 확인
    public boolean verifyBookmark(Exhibition exhibition, Member member) {
        return bookmarkRepository.existsByExhibitionAndMember(exhibition, member);
    }

    //조회수 증가
    @Transactional
    public void updateViewCount(Long id) {
        exhibitionRepository.updateViewCount(id);
    }

    //전시글 목록 조회
    public Page<Exhibition> getExhibitionList(String status, String type, String sort, String titleKeyword, int page) {
        PageRequest pageRequest;

        if (sort.equals("조회수순")) {
            pageRequest = PageRequest.of(page - 1, 8, Sort.by("viewCount").descending()
                    .and(Sort.by("id").descending()));
        } else if (sort.isEmpty()) {
            pageRequest = PageRequest.of(page - 1, 8, Sort.by("id").descending());
        }
        else {
            throw new CustomException(ExhibitionExceptionType.EXHIBITION_SORT_CRITERIA_ERROR);
        }
        return exhibitionRepository.searchPage(status, type, titleKeyword, pageRequest);
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
