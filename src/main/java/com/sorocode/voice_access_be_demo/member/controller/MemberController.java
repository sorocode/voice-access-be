package com.sorocode.voice_access_be_demo.member.controller;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.service.EnterLogService;
import com.sorocode.voice_access_be_demo.member.dto.PatchRequestDto;
import com.sorocode.voice_access_be_demo.member.dto.SignUpMultipartRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User API", description = "사용자 관련 기능을 담당하는 컨트롤러")
public class MemberController {

    private final MemberService memberService;
    private final EnterLogService enterLogService;

//    // 회원 등록
//    // JSON 요청을 받을 때 (Content-Type: application/json)
//    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "음성 없이 회원가입(application/json)")
//    public ResponseEntity<String> signupJson(
//            @RequestBody @Valid SignUpRequestDto signUpRequestDto
//    ) {
//        memberService.saveNewMember(signUpRequestDto, null); // 파일 없음
//        return ResponseEntity.ok("회원가입 성공");
//    }

    // multipart/form-data 요청을 받을 때 (Content-Type: multipart/form-data)
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입(multipart/form-data)")
    public ResponseEntity<?> signupMultipart(
            @ModelAttribute SignUpMultipartRequestDto request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("회원가입 데이터가 올바르지 않습니다.");
        }
        memberService.saveNewMember(request); // 파일 있음
        return ResponseEntity.ok(request.getUsername() + "님의 회원가입이 정상적으로 완료되었습니다!");
    }

    // 출입
    @PostMapping(value = "/login", consumes = {"multipart/form-data"})
    @Operation(summary = "로그인(multipart/form-data)")
    public Mono<ResponseEntity<String>> recognizeAudio(@RequestPart("audio") MultipartFile voiceFile) {
        return memberService.processAudioFile(voiceFile)
                .flatMap(phoneNumber -> {
                    // 음성 인식 성공 시 체크인 수행
                    try {
                        EnterLog enterLog = enterLogService.checkIn(phoneNumber);
                        System.out.println("체크인 시간 = " + enterLog.getCheckInTime());
                        Member loginMmber = memberService.getMemberByPhoneNumber(phoneNumber);
                        return Mono.just(ResponseEntity.ok(loginMmber.getName() + "님 환영합니다!"));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.internalServerError().body("체크인 실패: " + e.getMessage()));
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }


    // 조회
    // FIXME: 백엔드에서는 id 조회만 있어도 될 듯하긴 한데 일단 전화번호 및 이름 조회 추가함(추후 프론트엔드와 연동 시 수정)
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
    @Operation(summary = "userId로 특정 유저 조회")
    public ResponseEntity<Member> getMemberById(@PathVariable("userId") String userId) {
        Member memberById = memberService.getMemberById(userId);
        if (memberById == null) {
            return ResponseEntity.noContent().build(); // 204 No Content 반환
        }
        return ResponseEntity.ok(memberById);
    }


    // 삭제
    @DeleteMapping("/users/{userId}")
    @Operation(summary = "유저 Id를 통해 유저를 삭제")
    public ResponseEntity<Member> deleteMembersByUserId(@PathVariable("userId") String userId) {
        memberService.deleteMemberById(userId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 수정
    @PatchMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 수정(application/json)")
    public ResponseEntity<Member> updateMemberById(@PathVariable("userId") String userId, @RequestBody @Valid PatchRequestDto patchRequestDto) {
        memberService.updateMember(userId, patchRequestDto);
        return ResponseEntity.ok(memberService.getMemberById(userId));
    }
}
