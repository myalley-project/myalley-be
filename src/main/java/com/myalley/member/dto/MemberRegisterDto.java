package com.myalley.member.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {

    @NotBlank(message="email")
    @Email(message="email")
    private String email;

    @NotEmpty(message="password")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message ="password" )
    private String password;


    private String gender;


    private LocalDate birth;

    @NotEmpty(message="nickname")
    @Length(min=2,max=10,message="nickname")
    private String nickname;

    private Integer adminNo;



}
