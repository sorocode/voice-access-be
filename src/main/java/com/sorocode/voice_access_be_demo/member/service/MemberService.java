package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    void validateVoiceFile(MultipartFile voiceFile);

    String saveVoiceFile(MultipartFile voiceFile);

    void saveNewMember(SignUpRequestDto signUpRequestDto, MultipartFile voiceFile);

    String processAudioFile(MultipartFile audioFile);

}
