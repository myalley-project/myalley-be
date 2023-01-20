package com.myalley.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.member.options.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

    @NotEmpty(message="비밀번호 형식 오류")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message ="비밀번호 형식 오류" )
    private String password;

    @NotEmpty(message="닉네임 형식 오류")
    @Length(min=2,max=10,message="닉네임 형식 오류")
    private String nickname;

    @Enumerated(value= EnumType.STRING)
    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;

}
