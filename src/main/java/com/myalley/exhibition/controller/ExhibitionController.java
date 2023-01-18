package com.myalley.exhibition.controller;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.dto.request.ExhibitionRequest;
import com.myalley.exhibition.dto.request.ExhibitionUpdateRequest;
import com.myalley.exhibition.dto.response.ExhibitionBasicResponse;
import com.myalley.exhibition.dto.response.ExhibitionPageResponse;
//import com.myalley.exhibition.options.ExhibitionStatus;
//import com.myalley.exhibition.service.ExhibitionSearchService;
import com.myalley.exhibition.service.ExhibitionService;
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
public class ExhibitionController {

    private final ExhibitionService exhibitionService;
//    private final ExhibitionSearchService searchService;
    /**
     * 전시글 등록 요청
     * @param request 전시회 정보를 담은 request json body
     * @return "전시회 게시글 등록이 완료되었습니다." 메시지를 전달한다.
     * @author Hwadam
     * */
    @PostMapping("/api/exhibitions")
    public ResponseEntity save(@Valid @RequestBody ExhibitionRequest request) {
        exhibitionService.save(request);
        return ResponseEntity.ok("전시회 게시글 등록이 완료되었습니다.");
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
        return ResponseEntity.ok("전시회 게시글 수정이 완료되었습니다.");
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
        return ResponseEntity.ok("해당 전시회 게시글이 삭제되었습니다.");
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
//
//    /**
//     * 전시글 목록 조회 요청
//     * @author Hwadam
//     * */
//    @GetMapping("/exhibitions")
//    public Page<ExhibitionBasicResponse> getExhibitions(
//            @RequestParam(name = "status") final ExhibitionStatus status,
//            final PageInfoDto pageRequest) {
//        return searchService.search(status, pageRequest.of())
//                        .map(ExhibitionBasicResponse::new);
//    }
//
//    @GetMapping("/exhibitions")
//    public ResponseEntity getLists(
//            @Positive @RequestParam int page,
//            @Positive @RequestParam int size,
//            @RequestParam(name = "status") final ExhibitionStatus status) {
//        Page<Exhibition> paged = searchService.search(status, PageRequest.of(page, size,
//                Sort.by("id").descending()));
//        List<Exhibition> exhibitions = paged.getContent();
//        return new ResponseEntity<>(
//                new ExhibitionPageResponse<>(mapper.exhibitionsToExhibitionBasicResponses(exhibitions), paged),
//                HttpStatus.OK);
//    }
//

    //전시회 상태와 유형 같이 검색
    @GetMapping("/exhibitions")
    public ResponseEntity getExhibitions(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "type", required = true) String type) {
        int size = 8;
            Page<Exhibition> pageExhibitions = exhibitionService.readPageAllSearch(status, type, page, size);
            Page<ExhibitionBasicResponse> responsePage = pageExhibitions
                    .map(exhibition -> new ExhibitionBasicResponse(
                            exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                            exhibition.getPosterUrl(), exhibition.getDate().substring(0,10),
                            exhibition.getDate().substring(11,21), exhibition.getType(),
                            exhibition.getStatus(), exhibition.getViewCount()));
            List<ExhibitionBasicResponse> exhibitions = responsePage.getContent();
            return new ResponseEntity<>(
                    new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                    HttpStatus.OK);
        }



    //전시회 관람여부만 조회
    @GetMapping("/main/exhibitions")
    public ResponseEntity getExhibitionsAll(
            @Positive @RequestParam int page,
            @RequestParam(value = "status", required = true) String status) {
        int size = 8;
        Page<Exhibition> pageExhibitions = exhibitionService.readPageAll(status, page, size);
        Page<ExhibitionBasicResponse> responsePage = pageExhibitions
                .map(exhibition -> new ExhibitionBasicResponse(
                        exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                        exhibition.getPosterUrl(), exhibition.getDate().substring(0,10),
                        exhibition.getDate().substring(11,21), exhibition.getType(),
                        exhibition.getStatus(), exhibition.getViewCount()));
        List<ExhibitionBasicResponse> exhibitions = responsePage.getContent();

        return new ResponseEntity<>(
                new ExhibitionPageResponse<>(exhibitions, pageExhibitions),
                HttpStatus.OK);
    }

}
