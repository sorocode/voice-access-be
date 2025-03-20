package com.sorocode.voice_access_be_demo.member.entity;

import com.sorocode.voice_access_be_demo.global.BaseEntity;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@RequiredArgsConstructor
@Setter
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private LocalDate birthday;

    private Short height;

    private Short weight;

    private String voiceFileLocation;


}
