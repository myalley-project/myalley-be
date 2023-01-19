package com.myalley.mate.controller;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionMateListResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.dto.MatePageResponse;
import com.myalley.mate.dto.MateRequest;
import com.myalley.mate.dto.MateSimpleResponse;
import com.myalley.mate.dto.MateUpdateRequest;
import com.myalley.mate.service.MateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Long memberId = 1L;
        mateService.save(mateRequest, memberId);

        return ResponseEntity.ok("메이트 모집글 등록이 완료되었습니다.");
    }

    @PutMapping("/api/mates/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody MateUpdateRequest request) {
        Long memberId = 1L;
        mateService.update(id, request, memberId);
        return ResponseEntity.ok("메이트 모집글 수정이 완료되었습니다.");
    }

    @GetMapping("/mates/{id}")
    public ResponseEntity showMateDetail(@PathVariable Long id) {
        mateService.updateViewCount(id);
        return ResponseEntity.ok(mateService.findDetail(id));
    }

    @DeleteMapping("/api/mates/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Long memberId = 1L;
        mateService.delete(id, memberId);
        return ResponseEntity.ok("전시회 정보가 삭제되었습니다.");
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
