package com.sorocode.voice_access_be_demo.member.controller;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // JSON 요청을 받을 때 (Content-Type: application/json)
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signupJson(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto
    ) {
        memberService.saveNewMember(signUpRequestDto, null); // 파일 없음
        return ResponseEntity.ok("회원가입 성공");
    }

    // multipart/form-data 요청을 받을 때 (Content-Type: multipart/form-data)
    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<String> signupMultipart(
            @RequestPart(value = "data", required = false) @Valid SignUpRequestDto signUpRequestDto,
            @RequestPart(value = "voiceFile", required = false) MultipartFile voiceFile
    ) {
        memberService.saveNewMember(signUpRequestDto, voiceFile); // 파일 있음
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping(value = "/recognize", consumes = {"multipart/form-data"})
    public ResponseEntity<String> recognizeAudio(@RequestPart("audio") MultipartFile audioFile) {
        try {
            String result = memberService.processAudioFile(audioFile);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
