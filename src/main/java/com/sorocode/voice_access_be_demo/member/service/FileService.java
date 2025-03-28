package com.sorocode.voice_access_be_demo.member.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {
    Mono<String> sendOneVoiceFile(MultipartFile voiceFile);

    Mono<String> sendMultipleVoiceFile(String phoneNumber, List<MultipartFile> voiceFiles);

    File convertMultiPartToFile(MultipartFile file) throws IOException;
}
