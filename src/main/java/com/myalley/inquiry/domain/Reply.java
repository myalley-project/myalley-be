package com.myalley.inquiry.domain;

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

   // @ManyToOne
    private Long member_id;

    private Long inquiry_id;

    private String reply;

    @CreatedDate
    private LocalDate created_date;

}
