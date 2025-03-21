package com.sorocode.voice_access_be_demo.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
