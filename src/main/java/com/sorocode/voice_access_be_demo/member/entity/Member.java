package com.sorocode.voice_access_be_demo.member.entity;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

// TODO: 추후 빌더 패턴으로 리팩토링하기(Setter 최소화)
@Entity
@Setter
@Getter
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


    @OneToMany(mappedBy = "member")
    private List<EnterLog> enterLogs;

}
