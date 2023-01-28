package com.myalley.inquiry.service;

import com.myalley.inquiry.domain.Inquiry;
import com.myalley.inquiry.dto.InquiryDto;
import com.myalley.inquiry.repository.InquiryRepository;
import com.myalley.member.domain.Member;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    public ResponseEntity create(InquiryDto inquiryDto){

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        inquiryRepository.save(Inquiry.builder()
                        .title(inquiryDto.getTitle())
                        .type(inquiryDto.getType())
                        .content(inquiryDto.getContent())
                        .member(member)
                        .is_answered(false)
                        .build()
                );

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map, HttpStatus.OK);
    }
}
