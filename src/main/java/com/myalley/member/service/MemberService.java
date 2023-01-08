package com.myalley.member.service;

import com.myalley.member.domain.Member;
import com.myalley.member.dto.MemberRegisterDto;
import com.myalley.member.exception.MemberException;
import com.myalley.member.exception.MemberExceptionType;
import com.myalley.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    /**
     * 유저 등록
     *
     * @param email email
     * @param password password
     * @return 유저 권한을 가지고 있는 유저
     */
    public ResponseEntity signup(MemberRegisterDto memberRegisterDto) {
        if (memberRepository.findByEmail(memberRegisterDto.getEmail()) != null) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }else if(memberRepository.findByNickname(memberRegisterDto.getNickname())!=null){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }

        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname())
                .gender(memberRegisterDto.getGender())
                .birth(memberRegisterDto.getBirth())
                .authority("ROLE_USER")
                .status("활동중")
                .build());
        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);


    }

    /**
     * 관리자 등록
     *
     * @param username username
     * @param password password
     * @return 관리자 권한을 가지고 있는 유저
     */
    public ResponseEntity signupAdmin(MemberRegisterDto memberRegisterDto)
    {
        if (memberRepository.findByEmail(memberRegisterDto.getEmail()) != null) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }else if(memberRepository.findByNickname(memberRegisterDto.getNickname())!=null){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }
        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname())
                .authority("ROLE_ADMIN")
                .status("활동중")
                .adminNo(memberRegisterDto.getAdminNo())
                .build());

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
