package com.myalley.inquiry.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
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


    private Long member_id;

    private String type;

    private String content;

    private boolean is_answered;

    @CreatedDate
    private LocalDate created_at;



}
