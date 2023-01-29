package com.myalley.inquiry.domain;

import com.myalley.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="reply")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reply {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="reply_id")
    private Long replyId;

    @ManyToOne(targetEntity= Member.class,fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="inquiry_inquiry_id", referencedColumnName = "inquiry_id")
    private Inquiry inquiry;

    private String reply;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

}
