package com.sorocode.voice_access_be_demo.member.service;

import com.sorocode.voice_access_be_demo.member.dto.PatchRequestDto;
import com.sorocode.voice_access_be_demo.member.dto.SignUpMultipartRequestDto;
import com.sorocode.voice_access_be_demo.member.dto.StatsResponseDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface MemberService {
    void validateVoiceFile(MultipartFile voiceFile);

    void saveNewMember(SignUpMultipartRequestDto request);

    Mono<String> processAudioFile(MultipartFile audioFile);

    List<Member> getMembers();

    List<Member> getMembersByUsername(String username);

    Member getMemberByPhoneNumber(String phoneNumber);

    List<Member> getMembersByPhoneNumberSuffix(String last4Digits);

    Member getMemberById(String userId);

    void deleteMemberById(String userId);

    Member updateMember(String userId, PatchRequestDto patchRequestDto);

    StatsResponseDto getStats();
}
