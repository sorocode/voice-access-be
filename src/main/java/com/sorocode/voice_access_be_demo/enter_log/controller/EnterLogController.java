package com.sorocode.voice_access_be_demo.enter_log.controller;

import com.sorocode.voice_access_be_demo.enter_log.dto.EnterLogResponseDto;
import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.entity.EnterStatus;
import com.sorocode.voice_access_be_demo.enter_log.service.EnterLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "LOG API", description = "출입로그 관련 기능을 담당")
public class EnterLogController {
    private final EnterLogService enterLogService;


    @GetMapping("/logs/{userId}")
    @Operation(summary = "출입로그조회")
    public List<EnterLog> getEnterLogs(@PathVariable String userId) {
        return enterLogService.getMemberLogs(userId);
    }

    @GetMapping("/logs")
    public Result getEnterLogs() {
        List<EnterLog> findLogs = enterLogService.getAllLogs();
        List<EnterLogResponseDto> collect = findLogs.stream()
                .map(m -> new EnterLogResponseDto(m.getId(), m.getMember().getName(), m.getMember().getPhoneNumber(), m.getCheckInTime(), m.getCheckOutTime(), EnterStatus.ENTER))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


}

