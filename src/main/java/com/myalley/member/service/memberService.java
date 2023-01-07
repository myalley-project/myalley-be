package com.myalley.member.service;

import com.myalley.member.domain.Member;
import com.myalley.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class memberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 등록
     *
     * @param email email
     * @param password password
     * @return 유저 권한을 가지고 있는 유저
     */
    public Member signup(
            String email,
            String password
    ) {
        if (memberRepository.findByEmail(email) != null) {
            throw new AlreadyRegisteredUserException();
        }
        return memberRepository.save(new Member(email, passwordEncoder.encode(password), "ROLE_USER"));
    }

    /**
     * 관리자 등록
     *
     * @param username username
     * @param password password
     * @return 관리자 권한을 가지고 있는 유저
     */
    public Member signupAdmin(
            String username,
            String password
    ) {
        if (memberRepository.findByEmail(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        return memberRepository.save(new Member(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
