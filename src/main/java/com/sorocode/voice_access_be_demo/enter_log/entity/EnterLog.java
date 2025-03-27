package com.sorocode.voice_access_be_demo.enter_log.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EnterLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // Member와 연결
    private Member member;


}
