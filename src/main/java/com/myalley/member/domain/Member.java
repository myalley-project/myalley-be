package com.myalley.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @GeneratedValue
    @Column(name="member_id")
    @Id
    private Long memberId;
    @Column(name="email",unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="nickname",unique=true)
    private String nickname;

    @Column(name="gender")
    private String gender;

    @Column(name="birth")
    private LocalDate birth;

    @Column(name="admin_no",unique=true)
    private int adminNo;

    @Column(name="authority")
    private String authority;

    @Column(name="status")
    private String status;

    @Column(name="black_status")
    private String blackStatus;

    @Column(name="level")
    private String level;

    @Column(name="user_image")
    private String userImage;


    private LocalDate createdAt;

    private LocalDate modifiedAt;




    public Boolean isAdmin() {

        return authority.equals("ROLE_ADMIN");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
