package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

    @Override
    public String saveVoiceFile(MultipartFile voiceFile) throws IOException {
        // 저장할 디렉토리가 없으면 생성
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // 파일을 저장할 경로 설정
        File destinationFile = new File(uploadFolder, "recording.wav");

        // MultipartFile을 해당 경로에 저장
        voiceFile.transferTo(destinationFile);

        System.out.println("✅ 음성 파일 저장 완료: " + destinationFile.getAbsolutePath());
        return destinationFile.getAbsolutePath();
    }


    @Override
    public void saveNewMember(SignUpRequestDto signUpRequestDto, MultipartFile voiceFile) throws IOException {
        // 1. 회원 정보 저장
        Member newMember = new Member();
        newMember.setName(signUpRequestDto.getUsername());
        newMember.setPhoneNumber(signUpRequestDto.getPhoneNum());
        newMember.setAddress(signUpRequestDto.getHomeAddress());
        // 2. 음성 파일이 있으면 검증 후 저장
        if (voiceFile != null && !voiceFile.isEmpty()) {
            validateVoiceFile(voiceFile);
            String voiceFileLocation = saveVoiceFile(voiceFile);
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
