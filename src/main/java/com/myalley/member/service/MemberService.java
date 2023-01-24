package com.myalley.member.service;

import com.myalley.exception.BlogReviewExceptionType;
import com.myalley.member.dto.MemberInfoDto;
import com.myalley.member.dto.MemberUpdateDto;
import com.myalley.member.options.Authority;
import com.myalley.member.options.Level;
import com.myalley.member.options.Status;
import com.myalley.member.domain.Member;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;
@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    public ResponseEntity signup(MemberRegisterDto memberRegisterDto) {
        if (memberRepository.findByEmail(memberRegisterDto.getEmail()) != null) {
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }else if(memberRepository.findByNickname(memberRegisterDto.getNickname())!=null){
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }

        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname())
                .gender(memberRegisterDto.getGender())
                .birth(memberRegisterDto.getBirth())
                .level(Level.LEVEL1)
                .authority(Authority.ROLE_USER)//Authority.ROLE_USER
                .status(Status.활동중)
                .build());

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }


    public ResponseEntity signupAdmin(MemberRegisterDto memberRegisterDto)
    {
        if (memberRepository.findByEmail(memberRegisterDto.getEmail()) != null) {
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }else if(memberRepository.findByNickname(memberRegisterDto.getNickname())!=null){
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }

        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname()+ "."+  UUID.randomUUID().toString())
                .authority(Authority.ROLE_ADMIN)//Authority.ROLE_ADMIN
                .status(Status.활동중)
                .adminNo(memberRegisterDto.getAdminNo())
                .build());

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }

    public MemberInfoDto memberInfo(String email){
        Member member=memberRepository.findByEmail(email);
        String nickname=member.getNickname();
        if(member.isAdmin()){//관리자 닉네임 겹치지않게관리
            nickname=nickname.substring(nickname.indexOf("."));
        }
        return MemberInfoDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(nickname)
                .gender(member.getGender().name())
                .birth(member.getBirth())
                .level(member.getLevel().name())
                .memberImage(member.getUserImage())
                .authority(member.getAuthority().name())
                .build();
    }

    public ResponseEntity update(Member member){
        if(memberRepository.findByNickname(member.getNickname())!=null){
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }
        memberRepository.save(member);

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }


    public ResponseEntity delete(Long id) {

        memberRepository.deleteById(id);

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member verifyMember(Long memberId) {
        return  memberRepository.findById(memberId).orElseThrow(() ->{
            throw new CustomException(MemberExceptionType.NOT_FOUND_MEMBER);
        });
    }
}
