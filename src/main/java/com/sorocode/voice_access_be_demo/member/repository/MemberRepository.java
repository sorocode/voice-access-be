package com.sorocode.voice_access_be_demo.member.repository;

import com.sorocode.voice_access_be_demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
