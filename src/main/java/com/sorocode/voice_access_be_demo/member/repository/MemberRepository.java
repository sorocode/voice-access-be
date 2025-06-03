package com.sorocode.voice_access_be_demo.member.repository;

import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Member> getMemberByPhoneNumber(String phoneNumber);


    Member getMemberById(Long id);

    List<Member> getMembersByName(String username);

    // 전화번호 뒷 4자리로 조회 (Native Query 사용)
    @Query(value = "SELECT * FROM member WHERE RIGHT(phone_number, 4) = :last4Digits", nativeQuery = true)
    List<Member> getMemberByPhoneNumberSuffix(@Param("last4Digits") String last4Digits);

    // 성별 카운트
    Long countMemberByGender(GenderEnum genderEnum);
}
