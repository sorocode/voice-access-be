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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    public String saveVoiceFile(MultipartFile voiceFile, String userName) {
        validateVoiceFile(voiceFile);

        // 저장할 디렉토리 확인 및 생성
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // UUID 기반 고유 파일명 생성
        String uniqueFileName = "voice_" + userName + "_" + UUID.randomUUID() + ".wav";
        File destinationFile = new File(uploadFolder, uniqueFileName);

        try {
            // MultipartFile을 해당 경로에 저장
            Files.copy(voiceFile.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ 음성 파일 저장 완료: " + destinationFile.getParent());
            return destinationFile.getParent(); // 파일이 저장된 폴더명 리턴
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void saveNewMember(SignUpRequestDto signUpRequestDto, List<MultipartFile> voiceFiles) {
        // 회원 정보 저장
        Member newMember = new Member();
        newMember.setName(signUpRequestDto.getUsername());
        newMember.setPhoneNumber(signUpRequestDto.getPhoneNum());
        newMember.setAddress(signUpRequestDto.getHomeAddress());
        newMember.setHeight(signUpRequestDto.getHeight());
        newMember.setWeight(signUpRequestDto.getWeight());
        newMember.setGender(signUpRequestDto.getGender());
        newMember.setBirthday(signUpRequestDto.getBirthday());

        if (voiceFiles.size() == 5) {
            for (var voiceFile : voiceFiles) {
                validateVoiceFile(voiceFile);
                String voiceFileLocation = saveVoiceFile(voiceFile, signUpRequestDto.getUsername());
                newMember.setVoiceFileLocation(voiceFileLocation);
            }
        } else {
            throw new RuntimeException("파일 개수가 5개여야 합니다. 현재 파일 수: " + voiceFiles.size());
        }

        //  트랜잭션 안에서 WebClient 요청을 실행하지 않도록 분리
        memberRepository.save(newMember);

        //  비동기적으로 WebClient 요청 실행 (트랜잭션과 분리)
        fileService.sendMultipleVoiceFile(signUpRequestDto.getUsername(), voiceFiles)
                .doOnSuccess(response -> System.out.println("📩 파일 업로드 응답: " + response))
                .subscribe();  // 구독해서 요청 실행!
    }

    @Override
    public Mono<String> processAudioFile(MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("오디오 파일이 없습니다.");
        }
        // 비동기적으로 WebClient 요청 실행
        return fileService.sendOneVoiceFile(audioFile);
    }
}