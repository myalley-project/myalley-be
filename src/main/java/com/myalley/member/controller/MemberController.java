package com.myalley.member.controller;

import com.myalley.member.domain.Member;
import com.myalley.member.dto.MemberInfoDto;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.dto.MemberUpdateDto;
import com.myalley.member.jwt.JwtUtils;
import com.myalley.member.service.MemberService;
import com.myalley.member.service.ProfileS3Service;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor

public class MemberController {
    private final MemberService memberService;
    private final ProfileS3Service profileS3Service;
    private final PasswordEncoder passwordEncoder;
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
            @Valid @RequestPart(name = "data") MemberUpdateDto memberUpdateDto, @RequestPart(name="imageFile",required =false) MultipartFile multipartFile
            ) throws IOException {
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String url=member.getUserImage();

        if(!multipartFile.isEmpty()){//프로필 수정시 이미지 삭제 및 저장
            profileS3Service.deleteImage(url);
            url=profileS3Service.uploadImage(multipartFile);
        }

        member.update(memberUpdateDto,url);
        member.setPassword(passwordEncoder.encode(memberUpdateDto.getPassword()));

        return memberService.update(member);
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

    @GetMapping("/exhibitions/good")
    ResponseEntity good(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String,String> map=new HashMap<>();
        map.put("msg","배포 정상적으로 작동");
        return new ResponseEntity(map,HttpStatus.OK);
    }


}
