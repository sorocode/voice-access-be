package com.sorocode.voice_access_be_demo.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SignUpRequestDto {

    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 3, message = "이름은 최소 3자 이상이어야 합니다.")
    private String username;

    @NotNull(message = "전화번호는 필수 입력값입니다.")
    @Size(min = 8, message = "전화번호는 최소 8자 이상이어야 합니다.")
    private String phoneNum;

    private String homeAddress;

}