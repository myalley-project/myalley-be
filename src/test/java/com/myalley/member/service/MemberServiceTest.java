package com.myalley.member.service;

import com.myalley.member.dto.MemberRegisterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest
@ActiveProfiles(profiles="test")
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void signup() {
        MemberRegisterDto member1=MemberRegisterDto.builder()
                .email("test@naver.com")
                .password("1234")
                .nickname("dudwls")
                .build();
        MemberRegisterDto member2=MemberRegisterDto.builder()
                .email("test@naver.com")
                .password("12343")
                .nickname("dudwls5")
                .build();
        System.out.println(passwordEncoder.encode(member1.getPassword()));
        memberService.signup(member1);
        Assertions.assertEquals(memberService.findByEmail("test@naver.com").getEmail  (),"test@naver.com");
        //memberService.signup(member2);


    }

    @Test
    void signupAdmin() {
    }

    @Test
    void findByEmail() {
    }
}