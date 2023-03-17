package com.myalley.inquiry.service;

import com.myalley.exception.CustomException;
import com.myalley.exception.InquiryExceptionType;
import com.myalley.inquiry.domain.Inquiry;
import com.myalley.inquiry.domain.Reply;
import com.myalley.inquiry.dto.InquiryDetailDto;
import com.myalley.inquiry.dto.InquiryDto;
import com.myalley.inquiry.dto.MemberDetailDto;
import com.myalley.inquiry.dto.ReplyDetailDto;
import com.myalley.inquiry.repository.InquiryRepository;
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
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    public ResponseEntity createInquiry(InquiryDto inquiryDto){

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        inquiryRepository.save(Inquiry.builder()
                        .title(inquiryDto.getTitle())
                        .type(inquiryDto.getType())
                        .content(inquiryDto.getContent())
                        .member(member)
                        .isAnswered(false)
                        .build()
                );

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    public InquiryDetailDto findInquiryById(Long inquiryId){

        Inquiry inquiry=inquiryRepository.findById(inquiryId).orElseThrow(()->new CustomException(InquiryExceptionType.NOT_FOUND_INQUIRY));
        Reply reply=inquiry.getReply();
        Member member=inquiry.getMember();

        if(inquiry.isAnswered()){

        return InquiryDetailDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .content(inquiry.getContent())
                .type(inquiry.getType())
                .createdAt(inquiry.getCreatedAt())
                .reply(ReplyDetailDto.builder()
                        .replyId(reply.getReplyId())
                        .createdAt(reply.getCreatedAt())
                        .replier(reply.getMember().getNickname())
                        .reply(reply.getReply())
                        .build())
                .member(MemberDetailDto.builder()
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .build())
                .build();
        }
        else{

            return InquiryDetailDto.builder()
                    .inquiryId(inquiry.getInquiryId())
                    .content(inquiry.getContent())
                    .type(inquiry.getType())
                    .createdAt(inquiry.getCreatedAt())
                    .member(MemberDetailDto.builder()
                            .memberId(member.getMemberId())
                            .nickname(member.getNickname())
                            .build())
                    .build();

        }
    }
}
