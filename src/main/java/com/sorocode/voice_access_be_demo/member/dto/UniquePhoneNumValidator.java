package com.sorocode.voice_access_be_demo.member.dto;

import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneNumValidator implements ConstraintValidator<UniquePhoneNum, String> {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber != null && !memberRepository.existsByPhoneNumber(phoneNumber); // 이미 존재하면 false 반환
    }
}
