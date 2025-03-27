package com.sorocode.voice_access_be_demo.enter_log.controller;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.service.EnterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnterLogController {
    private final EnterLogService enterLogService;


    @GetMapping("/logs/{userId}")
    public List<EnterLog> getEnterLogs(@PathVariable Long userId) {
        return enterLogService.getMemberLogs(userId);
    }
}

