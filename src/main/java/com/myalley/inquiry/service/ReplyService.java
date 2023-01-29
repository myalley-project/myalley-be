package com.myalley.inquiry.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.InquiryExceptionType;
import com.myalley.inquiry.domain.Inquiry;
import com.myalley.inquiry.domain.Reply;
import com.myalley.inquiry.dto.ReplyDetailDto;
import com.myalley.inquiry.dto.ReplyDto;
import com.myalley.inquiry.repository.InquiryRepository;
import com.myalley.inquiry.repository.ReplyRepository;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final InquiryRepository inquiryRepository;


    public ResponseEntity create(ReplyDto replyDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Inquiry inquiry=inquiryRepository.findById(replyDto.getInquiryId()).orElseThrow(()->new CustomException(InquiryExceptionType.NOT_FOUND_INQUIRY));
        inquiry.setAnswered(true);
        replyRepository.save(Reply.builder()
                .reply(replyDto.getReply())
                .member(member)
                .inquiry(inquiry)
                .build());

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map, HttpStatus.OK);
    }
}
