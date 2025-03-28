package com.sorocode.voice_access_be_demo.enter_log.repository;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnterLogRepository extends JpaRepository<EnterLog, Long> {
    List<EnterLog> findByMemberId(Long memberId);

}
