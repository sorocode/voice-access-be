package com.sorocode.voice_access_be_demo.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter // Setter 제거
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 제한
@AllArgsConstructor // 모든 필드를 받는 생성자 추가
@Builder // 빌더 패턴 적용
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private LocalDate birthday;

    private Float height;

    private Float weight;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnterLog> enterLogs;

    // 값 변경을 위한 update 메서드 추가 (Setter 대신 사용)
    public Member update(String name, String phoneNumber, String address, GenderEnum gender, LocalDate birthday, Float height, Float weight) {
        return Member.builder()
                .id(this.id) // 기존 ID 유지
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .gender(gender)
                .birthday(birthday)
                .height(height)
                .weight(weight)
                .enterLogs(enterLogs)
                .build();
    }
}