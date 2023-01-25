package com.myalley.member.dto;

import com.myalley.member.options.Gender;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {


    @Email(message="이메일 형식 오류")
    private String email;


    @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}", message ="비밀번호 형식 오류" )
    private String password;

    @Enumerated(value= EnumType.STRING)
    private Gender gender;


    private LocalDate birth;


    @Length(min=2,max=10,message="닉네임 형식 오류")
    private String nickname;

    private Integer adminNo;



}
