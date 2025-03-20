package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {
    void validateVoiceFile(MultipartFile voiceFile);

    String saveVoiceFile(MultipartFile voiceFile) throws IOException;

    void saveNewMember(SignUpRequestDto signUpRequestDto, MultipartFile voiceFile) throws IOException;

    String processAudioFile(MultipartFile audioFile);

}
