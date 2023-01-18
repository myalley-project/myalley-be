package com.myalley.member.controller;

import com.myalley.member.domain.Member;
import com.myalley.member.dto.MemberInfoDto;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.jwt.JwtUtils;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor

public class MemberController {
    private final MemberService memberService;


    @PostMapping("/signup")
    public ResponseEntity signUp(
           @Valid @RequestBody MemberRegisterDto memberRegisterDto
    ) {
        if(memberRegisterDto.getAdminNo()!=null)
             return memberService.signupAdmin(memberRegisterDto);
        else
            return memberService.signup(memberRegisterDto);

    }

    @PutMapping("/api/me")
    public ResponseEntity updateMember(
            @Valid @RequestBody MemberRegisterDto memberRegisterDto
    ) {
        if(memberRegisterDto.getAdminNo()!=null)
            return memberService.signupAdmin(memberRegisterDto);
        else
            return memberService.signup(memberRegisterDto);

    }

    @GetMapping("api/me")
    ResponseEntity<MemberInfoDto> memberInfo(HttpServletRequest request, HttpServletResponse response) {


        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        return new ResponseEntity<MemberInfoDto>(memberService.memberInfo(member.getEmail()), HttpStatus.ACCEPTED);

    }

    @DeleteMapping("api/me/withdrawals")
    public ResponseEntity withdrawal(){
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.delete(member.getMemberId());

    }

}
