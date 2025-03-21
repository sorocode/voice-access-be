package com.sorocode.voice_access_be_demo.member.controller;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /// 회원 등록
    // JSON 요청을 받을 때 (Content-Type: application/json)
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signupJson(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto
    ) throws IOException {
        memberService.saveNewMember(signUpRequestDto, null); // 파일 없음
        return ResponseEntity.ok("회원가입 성공");
    }

    // multipart/form-data 요청을 받을 때 (Content-Type: multipart/form-data)
    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<?> signupMultipart(
            @RequestPart(value = "data") @Valid SignUpRequestDto signUpRequestDto,
            @RequestPart(value = "voiceFile", required = false) MultipartFile voiceFile
    ) throws IOException {
        if (signUpRequestDto == null) {
            throw new IllegalArgumentException("회원가입 데이터가 올바르지 않습니다.");
        }
        memberService.saveNewMember(signUpRequestDto, voiceFile); // 파일 있음
        return ResponseEntity.ok("회원가입 성공");
    }

    /// 출입
    @PostMapping(value = "/recognize", consumes = {"multipart/form-data"})
    public ResponseEntity<String> recognizeAudio(@RequestPart("audio") MultipartFile voiceFile) {
        try {
            String result = memberService.processAudioFile(voiceFile);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
