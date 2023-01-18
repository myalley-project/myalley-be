package com.myalley.member.service;

import com.myalley.member.dto.MemberInfoDto;
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
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }else if(memberRepository.findByNickname(memberRegisterDto.getNickname())!=null){
            throw new CustomException(MemberExceptionType.ALREADY_EXIST_NAME);
        }

        memberRepository.save(Member.builder()
                .email(memberRegisterDto.getEmail())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .nickname(memberRegisterDto.getNickname())
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

        return MemberInfoDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender().name())
                .birth(member.getBirth())
                .level(member.getLevel().name())
                .memberImage(member.getUserImage())
                .authority(member.getAuthority().name())
                .build();
    }

    /**
    *
    * 사용자 삭제
    *
     **/
    public ResponseEntity delete(Long id) {

        memberRepository.deleteById(id);

        HashMap<String,Integer> map=new HashMap<>();
        map.put("resultCode",200);
        return new ResponseEntity(map,HttpStatus.OK);
    }
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
