package com.myalley.member.service;

import com.myalley.member.domain.Member;
import com.myalley.exception.CustomException;
import com.myalley.exception.MemberExceptionType;
import com.myalley.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MemberDetailService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;
    @Override
    public Member loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(member==null){
            throw new CustomException(MemberExceptionType.NOT_FOUND_MEMBER);
        }
        return member;
    }
}
