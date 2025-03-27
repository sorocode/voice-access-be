package com.sorocode.voice_access_be_demo.enter_log.service;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;

import java.util.List;


public interface EnterLogService {
    // 체크인
    EnterLog checkIn(Long memberId);

    // 체크아웃
    EnterLog checkOut(Long memberId);

    // 로그 조회
    List<EnterLog> getMemberLogs(Long memberId);

    // TODO: 삭제, 수정 로직
}
