package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.PatchRequestDto;
import com.sorocode.voice_access_be_demo.member.dto.SignUpMultipartRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Override
    public void validateVoiceFile(MultipartFile voiceFile) {
        String contentType = voiceFile.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("음성 파일만 업로드 가능합니다. 현재 파일 타입: " + contentType);
        }
    }

    @Override
    @Transactional
    public void saveNewMember(SignUpMultipartRequestDto request) {
        List<MultipartFile> voiceFiles = request.getVoiceFiles();
        if (voiceFiles.size() != 5) {
            throw new RuntimeException("파일 개수가 5개여야 합니다. 현재 파일 수: " + voiceFiles.size());
        }

        for (MultipartFile voiceFile : voiceFiles) {
            validateVoiceFile(voiceFile);
        }

        // Flask 요청 완료까지 기다림
        String flaskResponse = fileService.sendMultipleVoiceFile(request.getPhoneNumber(), voiceFiles)
                .block(); // 동기적으로 대기

        System.out.println("📩 파일 업로드 응답: " + flaskResponse);

        // Flask 요청 성공 후 회원 저장
        Member newMember = Member.builder()
                .name(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getHomeAddress())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .build();

        memberRepository.save(newMember);
    }

    @Override
    public Mono<String> processAudioFile(MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("오디오 파일이 없습니다.");
        }
        validateVoiceFile(audioFile);
        // 비동기적으로 WebClient 요청 실행
        return fileService.sendOneVoiceFile(audioFile);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public List<Member> getMembersByUsername(String username) {
        return memberRepository.getMembersByName(username);
    }


    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        return memberRepository.getMemberByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException(phoneNumber + "라는 전화번호를 가진 유저를 찾지 못했습니다."));
    }

    @Override
    public List<Member> getMembersByPhoneNumberSuffix(String last4Digits) {
        List<Member> members = memberRepository.getMemberByPhoneNumberSuffix(last4Digits);
        if (members.isEmpty()) {
            throw new RuntimeException(last4Digits + "라는 전화번호 뒷 4자리를 가진 유저를 찾지 못했습니다.");
        }
        return members;
    }

    @Override
    public Member getMemberById(String userId) {
        Long id = Long.parseLong(userId);
        return memberRepository.getMemberById(id);
    }

    @Override
    @Transactional
    public void deleteMemberById(String userId) {
        Long id = Long.parseLong(userId); // String → Long 변환
        memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾지 못했습니다: " + userId)); // 유저 찾지 못했을 경우
        memberRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Member updateMember(String userId, PatchRequestDto patchRequestDto) {
        Long id = Long.parseLong(userId);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾지 못했습니다"));
        // update() 메서드 사용하여 새 객체 생성
        Member updatedMember = member.update(
                patchRequestDto.getUsername(),
                patchRequestDto.getPhoneNumber(),
                patchRequestDto.getHomeAddress(),
                patchRequestDto.getGender(),
                patchRequestDto.getBirthday(),
                patchRequestDto.getHeight(),
                patchRequestDto.getWeight()
        );
        return memberRepository.save(updatedMember);
    }


}