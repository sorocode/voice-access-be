package com.sorocode.voice_access_be_demo.member.dto;

import com.sorocode.voice_access_be_demo.member.dto.validator.UniquePhoneNum;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SignUpRequestDto {

    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 3, message = "이름은 최소 3자 이상이어야 합니다.")
    private String username;

    @UniquePhoneNum
    @NotNull(message = "전화번호는 필수 입력값입니다.")
    @Size(min = 8, message = "전화번호는 최소 8자 이상이어야 합니다.")
    private String phoneNumber;

    private String homeAddress;

    @Min(value = 20, message = "가능한 값을 입력해주세요")
    private Float weight;

    @Min(value = 80, message = "가능한 값을 입력해주세요")
    private Float height;

    private GenderEnum gender;

    private LocalDate birthday;

}