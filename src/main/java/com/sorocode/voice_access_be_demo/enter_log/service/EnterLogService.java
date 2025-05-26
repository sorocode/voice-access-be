package com.sorocode.voice_access_be_demo.enter_log.service;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;

import java.util.List;


public interface EnterLogService {
    // 체크인
    EnterLog checkIn(String phoneNumber);

    // 전화번호 4자리로 체크인
    EnterLog checkInByPhoneNumberSuffix(String phoneNumber);

    // 체크아웃
    EnterLog checkOut(Long logId);

    // 로그 조회
    List<EnterLog> getMemberLogs(Long memberId);

    // TODO: 삭제, 수정 로직
}
