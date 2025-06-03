package com.sorocode.voice_access_be_demo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponseDto {
    private Long total;
    private Long male;
    private Long female;
}
