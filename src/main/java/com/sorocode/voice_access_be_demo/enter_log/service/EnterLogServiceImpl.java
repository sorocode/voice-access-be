package com.sorocode.voice_access_be_demo.enter_log.service;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.repository.EnterLogRepository;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
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

    @Override
    @Transactional
    public EnterLog checkIn(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        EnterLog log = new EnterLog();
        log.setMember(member);
        log.setCheckInTime(LocalDateTime.now());

        return enterLogRepository.save(log);
    }

    @Override
    @Transactional
    public EnterLog checkOut(Long logId) {
        EnterLog log = enterLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("출입 로그가 존재하지 않습니다."));

        log.setCheckOutTime(LocalDateTime.now());
        return enterLogRepository.save(log);
    }

    @Override
    public List<EnterLog> getMemberLogs(Long memberId) {
        return enterLogRepository.findByMemberId(memberId);
    }
}
