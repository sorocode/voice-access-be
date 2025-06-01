package com.sorocode.voice_access_be_demo.enter_log.service;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.repository.EnterLogRepository;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.error.MemberNotFoundException;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnterLogServiceImpl implements EnterLogService {
    private final EnterLogRepository enterLogRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public EnterLog checkIn(String phoneNumber) {
        Member member = memberRepository.getMemberByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        EnterLog log = EnterLog.builder()
                .checkInTime(LocalDateTime.now())
                .member(member)
                .build();
        return enterLogRepository.save(log);
    }

    @Override
    public EnterLog checkInByPhoneNumberSuffix(String last4Digits) {
        List<Member> members = memberRepository.getMemberByPhoneNumberSuffix(last4Digits);
        if (members.isEmpty()) {
            throw new MemberNotFoundException("해당 전화번호 뒷자리에 해당하는 회원이 없습니다. 다시 시도해주세요.");
        }
        Member member = members.get(0);
        EnterLog log = EnterLog.builder()
                .checkInTime(LocalDateTime.now())
                .member(member)
                .build();
        return enterLogRepository.save(log);
    }


    @Override
    @Transactional
    public EnterLog checkOut(Long logId) {
        EnterLog log = enterLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("출입 로그가 존재하지 않습니다."));
        log.updateCheckOutTime(LocalDateTime.now());
        return enterLogRepository.save(log);
    }

    @Override
    public List<EnterLog> getMemberLogs(String memberId) {
        Long id = Long.parseLong(memberId);
        return enterLogRepository.findByMemberId(id);
    }

    @Override
    public List<EnterLog> getLogsByMemberName(String name) {
        List<Member> membersByUsername = memberService.getMembersByUsername(name);
        Long id = membersByUsername.get(0).getId();
        return enterLogRepository.findByMemberId(id);
    }

    @Override
    public List<EnterLog> getAllLogs() {
        return enterLogRepository.findAll();
    }
}
