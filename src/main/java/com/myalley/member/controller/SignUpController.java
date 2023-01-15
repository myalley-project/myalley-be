package com.myalley.member.controller;

import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {
    private final MemberService memberService;



    /**
     * @return 회원가입 페이지 리소스
     */
    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public ResponseEntity signup(
           @Valid @RequestBody MemberRegisterDto memberRegisterDto
    ) {
        if(memberRegisterDto.getAdminNo()!=null)
             return memberService.signupAdmin(memberRegisterDto);
        else
            return memberService.signup(memberRegisterDto);

        // 회원가입 후 로그인 페이지로 이동
        //return "redirect:login";
    }
}
