package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {
    void validateVoiceFile(MultipartFile voiceFile);

    String saveVoiceFile(MultipartFile voiceFile, String userName);

    void saveNewMember(SignUpRequestDto signUpRequestDto, List<MultipartFile> voiceFiles);

    String processAudioFile(MultipartFile audioFile);

}
