package com.sorocode.voice_access_be_demo.enter_log.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EnterLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;


}
