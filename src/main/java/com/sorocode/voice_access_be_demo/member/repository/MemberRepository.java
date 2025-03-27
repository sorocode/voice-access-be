package com.sorocode.voice_access_be_demo.member.repository;

import com.sorocode.voice_access_be_demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Member> getMemberByPhoneNumber(String phoneNumber);

    Member getMemberById(Long id);

    List<Member> getMembersByName(String username);
}
