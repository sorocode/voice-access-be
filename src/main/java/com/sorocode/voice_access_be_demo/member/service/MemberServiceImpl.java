package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Value("${file.upload-dir}")  // application.yml에서 설정한 경로 가져오기
    private String uploadDir;

    @Override
    public void validateVoiceFile(MultipartFile voiceFile) {
        String contentType = voiceFile.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("음성 파일만 업로드 가능합니다.");
        }
    }


    @Override
    @Transactional
    public void saveNewMember(SignUpRequestDto signUpRequestDto, List<MultipartFile> voiceFiles) {
        // 회원 정보 저장
        Member newMember = new Member();
        newMember.setName(signUpRequestDto.getUsername());
        newMember.setPhoneNumber(signUpRequestDto.getPhoneNumber());
        newMember.setAddress(signUpRequestDto.getHomeAddress());
        newMember.setHeight(signUpRequestDto.getHeight());
        newMember.setWeight(signUpRequestDto.getWeight());
        newMember.setGender(signUpRequestDto.getGender());
        newMember.setBirthday(signUpRequestDto.getBirthday());

        if (voiceFiles.size() == 5) {
            for (var voiceFile : voiceFiles) {
                validateVoiceFile(voiceFile);
            }
            //  비동기적으로 WebClient 요청 실행
            fileService.sendMultipleVoiceFile(signUpRequestDto.getUsername(), voiceFiles)
                    .doOnSuccess(response -> System.out.println("📩 파일 업로드 응답: " + response))
                    .subscribe();  // 구독해서 요청 실행!
        } else {
            throw new RuntimeException("파일 개수가 5개여야 합니다. 현재 파일 수: " + voiceFiles.size());
        }

        memberRepository.save(newMember);

    }

    @Override
    public Mono<String> processAudioFile(MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("오디오 파일이 없습니다.");
        }
        // 비동기적으로 WebClient 요청 실행
        return fileService.sendOneVoiceFile(audioFile);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        return memberRepository.getMemberByPhoneNumber(phoneNumber);
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
        memberRepository.deleteById(id);
    }


}