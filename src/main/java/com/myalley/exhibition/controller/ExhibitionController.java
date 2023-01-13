package com.myalley.exhibition.controller;

import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    /**
     * 전시글 등록 요청
     * @param request 전시회 정보를 담은 request json body
     * @return 방금 등록한 전시회 정보의 id,title,시작일,종료일,조회수,이미지url을 전달한다.
     * @author Hwadam
     * */
    @PostMapping("/api/exhibitions")
    public ResponseEntity save(@Valid @RequestBody ExhibitionRequest request) {
        return ResponseEntity.ok(exhibitionService.save(request));
    }

    /**
     * 전시글 수정 요청
     * @param updateRequest 전시회 정보 수정을 위한 정보를 담은 request body
     * @param id 수정하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 수정 요청 성공 시 수정 완료 문자열 메시지가 리턴된다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
    @PutMapping("/api/exhibitions/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @Valid @RequestBody ExhibitionUpdateRequest updateRequest) {
            exhibitionService.update(updateRequest, id);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    /**
     * 전시글 삭제 요청
     * @param id 삭제하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 수정 요청 성공 시 수정 완료 문자열 메시지가 리턴된다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
    @DeleteMapping("/api/exhibitions/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        exhibitionService.delete(id);
        return ResponseEntity.ok("전시회 정보가 삭제되었습니다.");
    }

    /**
     * 전시글 상세페이지 조회 요청
     * @param id 조회하려는 전시글 정보에 해당하는 id를 파라미터로 보낸다.
     * @return 파라미터로 보낸 id에 해당하는 게시글이 존재하면 전시글 정보를 담은 ExhibitionResponse 를 리턴하고 조회수 카운트를 증가시킨다.
     *         만약 파라미터로 보낸 id에 해당하는 게시글이 존재하지 않는다면 게시글이 존재하지 않는다는 Exception 메시지와 코드를 리턴한다.
     * @author Hwadam
     * */
    @GetMapping("/api/exhibitions/{id}")
    public ResponseEntity read(@PathVariable Long id) {
        exhibitionService.updateViewCount(id);
        return ResponseEntity.ok(exhibitionService.findInfo(id));
    }

    /**
     * 전시글 목록 조회 요청
     * @author Hwadam
     * */
    @GetMapping("/exhibitions")
    public ResponseEntity readAllExhibitions() {

        return ResponseEntity.ok("로직 구현 후 수정 예정");
    }
}
