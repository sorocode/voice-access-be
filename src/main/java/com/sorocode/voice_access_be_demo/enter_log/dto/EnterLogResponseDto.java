package com.sorocode.voice_access_be_demo.enter_log.dto;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnterLogResponseDto {
    private Long id;
    private String memberName;
    private String phoneNumber;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private EnterStatus status;
}