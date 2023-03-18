package com.myalley.inquiry.controller;

//import com.myalley.inquiry.Service.InquiryService;
import com.myalley.inquiry.dto.InquiryDetailDto;
import com.myalley.inquiry.service.InquiryService;
import com.myalley.inquiry.dto.InquiryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/inquiries")
    public ResponseEntity createInquiry(
            @Valid @RequestBody InquiryDto inquiryDto
    ) {
        return inquiryService.createInquiry(inquiryDto);
    }

    @GetMapping("/inquiries")
    public InquiryDetailDto findInquiryById(
            @RequestParam("inquiryId") Long inquiryId){

        return inquiryService.findInquiryById(inquiryId);
    }






}
