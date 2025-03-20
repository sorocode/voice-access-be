package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void validateVoiceFile(MultipartFile voiceFile) {
        String contentType = voiceFile.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("음성 파일만 업로드 가능합니다.");
        }
    }

    @Override
    public String saveVoiceFile(MultipartFile voiceFile) {
        try {
            File destination = new File("uploads/" + voiceFile.getOriginalFilename());
            voiceFile.transferTo(destination);
            System.out.println("파일 저장 완료: " + destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }


    @Override
    public void saveNewMember(SignUpRequestDto signUpRequestDto, MultipartFile voiceFile) {
        // 1. 회원 정보 저장
        Member newMember = new Member();
        newMember.setName(signUpRequestDto.getName());
        newMember.setPhoneNumber(signUpRequestDto.getPhoneNumber());
        newMember.setAddress(signUpRequestDto.getAddress());
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
