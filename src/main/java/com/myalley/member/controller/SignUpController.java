package com.myalley.member.controller;

import com.myalley.member.dto.MemberRegisterDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String signup(
            @ModelAttribute MemberRegisterDto memberRegisterDto
    ) {
        memberService.signup(memberRegisterDto.getUsername(), memberRegisterDto.getPassword());

        // 회원가입 후 로그인 페이지로 이동
        return "redirect:login";
    }
}
