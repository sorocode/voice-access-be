package com.sorocode.voice_access_be_demo.member.controller;

import com.sorocode.voice_access_be_demo.member.dto.PatchRequestDto;
import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 등록
    // JSON 요청을 받을 때 (Content-Type: application/json)
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signupJson(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto
    ) {
        memberService.saveNewMember(signUpRequestDto, null); // 파일 없음
        return ResponseEntity.ok("회원가입 성공");
    }

    // multipart/form-data 요청을 받을 때 (Content-Type: multipart/form-data)
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signupMultipart(
            @RequestPart(value = "data") @Valid SignUpRequestDto signUpRequestDto,
            @RequestPart(value = "voiceFiles", required = false) List<MultipartFile> voiceFiles
    ) {
        if (signUpRequestDto == null) {
            throw new IllegalArgumentException("회원가입 데이터가 올바르지 않습니다.");
        }
        memberService.saveNewMember(signUpRequestDto, voiceFiles); // 파일 있음
        return ResponseEntity.ok("회원가입 성공");
    }

    // 출입
    @PostMapping(value = "/login", consumes = {"multipart/form-data"})
    public Mono<ResponseEntity<String>> recognizeAudio(@RequestPart("audio") MultipartFile voiceFile) {
        return memberService.processAudioFile(voiceFile)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }


    // 조회
    @GetMapping("/users")
    public ResponseEntity<?> getMembers(@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                        @RequestParam(value = "username", required = false) String username) {
        if (phoneNumber == null) {
            List<Member> members;
            if (username == null) { // 전체조회
                members = memberService.getMembers();
            } else { // 유저명으로 조회
                members = memberService.getMembersByUsername(username);
            }
            if (members.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(members);

        } else { // 전화번호로 조회
            Member member = memberService.getMemberByPhoneNumber(phoneNumber);
            if (member == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(member);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Member> getMemberById(@PathVariable("userId") String userId) {
        Member memberById = memberService.getMemberById(userId);
        if (memberById == null) {
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }
        return ResponseEntity.ok(memberById);
    }


    // 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Member> deleteMembersByUserId(@PathVariable("userId") String userId) {
        memberService.deleteMemberById(userId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 수정
    @PatchMapping("/users/{userId}")
    public ResponseEntity<Member> updateMemberById(@PathVariable("userId") String userId, @RequestBody @Valid PatchRequestDto patchRequestDto) {
        memberService.updateMember(userId, patchRequestDto);
        return ResponseEntity.ok(memberService.getMemberById(userId));
    }
}
