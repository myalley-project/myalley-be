package com.myalley.inquiry.controller;

//import com.myalley.inquiry.Service.InquiryService;
import com.myalley.inquiry.service.InquiryService;
import com.myalley.inquiry.dto.InquiryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor

public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity createInquiry(
            @Valid @RequestBody InquiryDto inquiryDto
    ) {
        return inquiryService.create(inquiryDto);
    }
}
