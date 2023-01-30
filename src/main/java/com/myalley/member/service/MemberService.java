package com.myalley.member.service;

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
import com.myalley.member_deleted.domain.MemberDeleted;
import com.myalley.member_deleted.repository.MemberDeletedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;
@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberDeletedRepository memberDeletedRepository;


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
                .memberImage("")
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
        }else if(memberRepository.findByAdminNo(memberRegisterDto.getAdminNo())!=null){
            throw new CustomException(MemberExceptionType.WRONG_ADMINNO);
        }

        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname()+ "."+  UUID.randomUUID().toString())
                .authority(Authority.ROLE_ADMIN)//Authority.ROLE_ADMIN
                .status(Status.활동중)
                .memberImage("")
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
            nickname=nickname.substring(0,nickname.indexOf("."));
            return MemberInfoDto.builder()
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .nickname(nickname)
                    .gender("관리자")
                    .birth(null)
                    .level("관리자")
                    .memberImage(member.getMemberImage())
                    .authority(member.getAuthority().name())
                    .age(0)
                    .build();
        }

        LocalDate now=LocalDate.now(ZoneId.of("Asia/Seoul"));

        int age=now.getYear()-member.getBirth().getYear()-1;

        if(now.getDayOfYear()-member.getBirth().getDayOfYear()<=0)
            age++;
        return MemberInfoDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(nickname)
                .gender(member.getGender().name())
                .birth(member.getBirth())
                .level(member.getLevel().name())
                .memberImage(member.getMemberImage())
                .authority(member.getAuthority().name())
                .age(age)
                .build();
    }

    public ResponseEntity update(MemberUpdateDto memberUpdateDto,String url){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(memberRepository.findByNickname(memberUpdateDto.getNickname())!=null&&!memberUpdateDto.getNickname().equals(member.getNickname())){
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }

        member.update(memberUpdateDto,url);

        if(memberUpdateDto.getPassword()!=null)
            member.setPassword(passwordEncoder.encode(memberUpdateDto.getPassword()));


        memberRepository.save(member);

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }


    public ResponseEntity delete(Member member) {

        memberDeletedRepository.save(new MemberDeleted(member));
        memberRepository.delete(member);

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
