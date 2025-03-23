package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

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
            System.out.println("✅ 음성 파일 저장 완료: " + destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void saveNewMember(SignUpRequestDto signUpRequestDto, MultipartFile voiceFile) {
        // 1. 회원 정보 저장
        Member newMember = new Member();
        newMember.setName(signUpRequestDto.getUsername());
        newMember.setPhoneNumber(signUpRequestDto.getPhoneNum());
        newMember.setAddress(signUpRequestDto.getHomeAddress());

        // 2. 음성 파일이 있으면 검증 후 저장 (파일 저장은 트랜잭션과 분리)
        if (voiceFile != null && !voiceFile.isEmpty()) {
            validateVoiceFile(voiceFile);
            String voiceFileLocation = saveVoiceFile(voiceFile, signUpRequestDto.getUsername());
            newMember.setVoiceFileLocation(voiceFileLocation);
        }

        memberRepository.save(newMember);
    }

    @Override
    public String processAudioFile(MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("No audio file received");
        }

        // 파일 저장 또는 음성 인식 처리 로직
        String fileName = audioFile.getOriginalFilename();
        long fileSize = audioFile.getSize();
        System.out.println("fileName = " + fileName);
        return "Processed file: " + fileName + ", size: " + fileSize + " bytes";
    }
}