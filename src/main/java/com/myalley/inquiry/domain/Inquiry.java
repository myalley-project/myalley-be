package com.myalley.inquiry.domain;

import com.myalley.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
public class Inquiry {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="inquiry_id")
    private Long inquiryId;

    @ManyToOne(targetEntity=Member.class,fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private String type;

    @NotNull
    private String content;

    @Column(name="is_answered")
    private boolean isAnswered;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @OneToOne(mappedBy="inquiry")
    private Reply reply;



}
