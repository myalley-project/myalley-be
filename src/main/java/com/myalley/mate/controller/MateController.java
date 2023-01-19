package com.myalley.mate.controller;

import com.myalley.exhibition.dto.response.ExhibitionMateListResponse;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MatePageResponse;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.service.MateService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    @PostMapping("/api/mates")
    public ResponseEntity save(@Valid @RequestBody MateRequest mateRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.save(mateRequest, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("메이트 모집글 등록이 완료되었습니다.", headers, HttpStatus.OK);
    }

    @PutMapping("/api/mates/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody MateUpdateRequest request) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.update(id, request, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>("메이트 모집글 수정이 완료되었습니다.", headers, HttpStatus.OK);
    }

    @GetMapping("/mates/{id}")
    public ResponseEntity showMateDetail(@PathVariable Long id) {
        mateService.updateViewCount(id);
        return ResponseEntity.ok(mateService.findDetail(id));
    }

    @DeleteMapping("/api/mates/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        mateService.delete(id, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("메이트 모집글 삭제가 완료되었습니다.", headers, HttpStatus.OK);
    }

    //메이트글 모집완료 여부 목록 조회
    @GetMapping("/mates")
    public ResponseEntity findMateAll(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = false) String status) {
        int size = 4;
        Page<Mate> pageMate = mateService.readPageAll(status, page, size);
        Page<MateSimpleResponse> responsePage = pageMate
                .map(mate -> new MateSimpleResponse(
                        mate.getId(), mate.getTitle(),mate.getAvailableDate(),
                        mate.getStatus(), mate.getMateGender(), mate.getMateAge(),
                        mate.getCreatedAt(), mate.getViewCount(), mate.getMember().getMemberId(),
                        mate.getMember().getNickname(), new ExhibitionMateListResponse(
                        mate.getExhibition().getId(), mate.getExhibition().getTitle(),
                        mate.getExhibition().getSpace(), mate.getExhibition().getPosterUrl(),
                        mate.getExhibition().getStatus()
                )));
        List<MateSimpleResponse> mateList = responsePage.getContent();

        return new ResponseEntity<>(
                new MatePageResponse<>(mateList, pageMate),
                HttpStatus.OK);
    }
}
