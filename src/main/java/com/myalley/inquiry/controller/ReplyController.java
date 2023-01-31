package com.myalley.inquiry.controller;


import com.myalley.inquiry.dto.ReplyDto;
import com.myalley.inquiry.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replies")
    public ResponseEntity createInquiry(
            @Valid @RequestBody ReplyDto replyDto
    ) {

        return replyService.create(replyDto);
    }

}
