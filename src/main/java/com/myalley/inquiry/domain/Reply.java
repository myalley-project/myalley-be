package com.myalley.inquiry.domain;

import com.myalley.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="reply")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reply_id;

    @ManyToOne(targetEntity= Member.class,fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne
    private Inquiry inquiry;

    private String reply;

    @CreatedDate
    private LocalDate created_date;

}
