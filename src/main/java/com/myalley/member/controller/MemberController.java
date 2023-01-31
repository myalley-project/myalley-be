package com.myalley.member.controller;

import com.myalley.member.domain.Member;
import com.myalley.member.dto.MemberInfoDto;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.dto.MemberUpdateDto;
import com.myalley.member.jwt.JwtUtils;
import com.myalley.member.service.MemberService;
import com.myalley.member.service.ProfileS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
@Log
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ProfileS3Service profileS3Service;

    @PostMapping("/signup")
    public ResponseEntity signUp(
           @Valid @RequestBody MemberRegisterDto memberRegisterDto
    ) {
        log.info("유저 회원가입");
        if(memberRegisterDto.getAdminNo()!=null)
             return memberService.signupAdmin(memberRegisterDto);
        else
            return memberService.signup(memberRegisterDto);

    }


    @PutMapping("/api/me")
    public ResponseEntity updateMember(@Valid @RequestPart(value = "data") MemberUpdateDto memberUpdateDto,
            @RequestPart(value="imageFile",required =false) MultipartFile multipartFile//
            ) throws IOException {//
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String url=member.getMemberImage();
        log.info("유저 본인정보 수정");

        if(multipartFile!=null){//프로필 수정시 이미지 삭제 및 저장
            profileS3Service.deleteImage(url);
            url=profileS3Service.uploadImage(multipartFile);
        }



        return memberService.update(memberUpdateDto,url);
    }



    @GetMapping("api/me")
    ResponseEntity<MemberInfoDto> memberInfo(HttpServletRequest request, HttpServletResponse response) {

        log.info("본인정보 조회");
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<MemberInfoDto>(memberService.memberInfo(member.getEmail()), HttpStatus.OK);
    }

    @DeleteMapping("api/me/withdrawals")
    public ResponseEntity withdrawal(){

        log.info("일반유저 회원탈퇴");
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return memberService.delete(member);
    }



    @GetMapping("/exhibitions/good")
    ResponseEntity good(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String,String> map=new HashMap<>();
        map.put("msg","배포 정상적으로 작동");
        return new ResponseEntity(map,HttpStatus.OK);
    }


}
