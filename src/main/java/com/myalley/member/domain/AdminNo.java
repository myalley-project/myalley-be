package com.myalley.member.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admin_no")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
public class AdminNo {

    @Id
    @Column(name="admin_no")
    private Long adminNo;

    @Column(name="is_registered")
    private Boolean isRegistered;


    private String role;


}
