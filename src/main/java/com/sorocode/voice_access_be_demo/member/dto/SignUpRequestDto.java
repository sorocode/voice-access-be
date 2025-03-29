package com.sorocode.voice_access_be_demo.member.dto;

import com.sorocode.voice_access_be_demo.member.dto.validator.UniquePhoneNum;
import com.sorocode.voice_access_be_demo.member.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @UniquePhoneNum
    @NotNull(message = "전화번호는 필수 입력값입니다.")
    @Size(min = 8, message = "전화번호는 최소 8자 이상이어야 합니다.")
    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "사용자 주소", example = "대구광역시 북구 대학로 80 경북대학교")
    private String homeAddress;

    @Min(value = 20, message = "가능한 값을 입력해주세요")
    @Schema(description = "몸무게", example = "67.23")
    private Float weight;

    @Schema(description = "키", example = "167.23")
    @Min(value = 80, message = "가능한 값을 입력해주세요")
    private Float height;

    @Schema(description = "성별", example = "MALE")
    private GenderEnum gender;

    @Schema(description = "생년월일")
    private LocalDate birthday;

}