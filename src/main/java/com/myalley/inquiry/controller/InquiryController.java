package com.myalley.inquiry.controller;

//import com.myalley.inquiry.Service.InquiryService;
import com.myalley.inquiry.dto.InquiryDetailDto;
import com.myalley.inquiry.service.InquiryService;
import com.myalley.inquiry.dto.InquiryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
        return inquiryService.create(inquiryDto);
    }

    @GetMapping("/inquiries")
    public InquiryDetailDto find(
            @RequestParam("inquiryId") Long inquiryId){

        return inquiryService.find(inquiryId);
    }






}
