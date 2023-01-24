package com.myalley.member_deleted.domain;

import com.myalley.member.domain.Member;
import com.myalley.member.options.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="member_deleted")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
public class MemberDeleted {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    @Id
    private Long memberId;
    @Column(name="email",unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="nickname",unique=true)
    private String nickname;

    @Enumerated(value= EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @Column(name="birth")
    private LocalDate birth;

    @Column(name="admin_no")
    private int adminNo;

    @Enumerated(value= EnumType.STRING)
    @Column(name="authority")
    private Authority authority;

    @Enumerated(value= EnumType.STRING)
    @Column(name="status")
    private Status status;

    @Enumerated(value= EnumType.STRING)
    @Column(name="black_status")
    private BlackStatus blackStatus;

    @Enumerated(value= EnumType.STRING)
    @Column(name="level")
    private Level level;

    @Column(name="user_image")
    private String UserImage;


    @Column
    private LocalDateTime createdAt;

    @CreatedDate
    @Column
    private LocalDateTime deletedAt;

    public MemberDeleted(Member member){
        this.adminNo=member.getAdminNo();
        this.createdAt=member.getCreatedAt();
        this.authority=member.getAuthority();
        this.status=member.getStatus();
        this.birth=member.getBirth();
        this.memberId=member.getMemberId();
        this.level=member.getLevel();
        this.email=member.getEmail();
        this.nickname=member.getNickname();
        this.blackStatus=member.getBlackStatus();
        this.gender=member.getGender();
        this.password=member.getPassword();


    }
}
