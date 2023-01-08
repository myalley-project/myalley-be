package com.myalley.member.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {

    @NotBlank
    @Email
    private String email;

    @NotEmpty
    @Length(min=8,max=16)
    private String password;


    private String gender;


    private LocalDate birth;

    @NotEmpty
    @Length(min=2,max=10)
    private String nickname;

    private Integer adminNo;



}
