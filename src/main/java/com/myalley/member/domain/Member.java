package com.myalley.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.common.domain.BaseTime;
import com.myalley.member.dto.MemberUpdateDto;
import com.myalley.member.options.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
public class Member extends BaseTime implements UserDetails {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="member_id")
    @Id
    private Long memberId;
    @Column(name="email",unique=true)
    private String email;

    @NotNull
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="nickname",unique=true)
    private String nickname;

    @Enumerated(value= EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @Column(name="birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @Column(name="admin_no")
    private Long adminNo;

    @NotNull
    @Enumerated(value= EnumType.STRING)
    @Column(name="authority")
    private Authority authority;

    @NotNull
    @Enumerated(value= EnumType.STRING)
    @Column(name="status")
    private Status status;

    @Enumerated(value= EnumType.STRING)
    @Column(name="black_status")
    private BlackStatus blackStatus;

    @Enumerated(value= EnumType.STRING)
    @Column(name="level")
    private Level level;

    @Column(name="member_image")
    private String memberImage;


    public void update(MemberUpdateDto memberUpdateDto,String url){
        this.setMemberImage(url);
        this.setNickname(memberUpdateDto.getNickname());
      //  this.setGender(memberUpdateDto.getGender());
        this.setBirth(memberUpdateDto.getBirth());

    }


    public Boolean isAdmin() {

        return authority.name().equals("ROLE_ADMIN");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority.name());
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
