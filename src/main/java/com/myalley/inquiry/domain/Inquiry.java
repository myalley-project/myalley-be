package com.myalley.inquiry.domain;

import com.myalley.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.Identity;
import java.time.LocalDate;

@Entity
@Table(name="inquiry")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long inquiry_id;

    @ManyToOne(targetEntity=Member.class,fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private String type;

    @NotNull
    private String content;

    private boolean is_answered;

    @CreatedDate
    private LocalDate created_at;



}
