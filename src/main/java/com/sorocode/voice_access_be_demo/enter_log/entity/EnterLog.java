package com.sorocode.voice_access_be_demo.enter_log.entity;

import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호
@AllArgsConstructor
@Builder
public class EnterLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // Member와 연결
    private Member member;

    // 값 변경을 위한 메서드 추가 (Setter 제거)
    public void updateCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}