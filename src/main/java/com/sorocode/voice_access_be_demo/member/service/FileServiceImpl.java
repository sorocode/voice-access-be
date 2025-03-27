package com.sorocode.voice_access_be_demo.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final WebClient webClient;

    @Override
    public Mono<String> sendOneVoiceFile(MultipartFile voiceFile) { // 로그인용(음성파일 하나만 받음)
        try {
            // MultipartFile을 File로 변환
            File convertedFile = convertMultiPartToFile(voiceFile);

            return webClient.post()
                    .uri("/login")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("audio", new FileSystemResource(convertedFile)))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                            Mono.error(new RuntimeException("Client error: " + response.statusCode())))
                    .onStatus(HttpStatusCode::is5xxServerError, response ->
                            Mono.error(new RuntimeException("Server error: " + response.statusCode())))
                    .bodyToMono(String.class);

        } catch (IOException e) {
            return Mono.error(new RuntimeException("파일 변환 중 오류 발생", e));
        }
    }

    // TODO: 원할한 로그인을 위해 유저명 이외에 다른 파라미터(전화번호 등)으로 변경 혹은 추가할 예정
    @Override
    public Mono<String> sendMultipleVoiceFile(String username, List<MultipartFile> voiceFiles) { // 회원등록용(유저명 및 음성파일들 받음)
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        // 이름 추가
        builder.part("username", username);

        // 파일을 직접 변환 후 추가
        for (MultipartFile file : voiceFiles) {
            try {
                File convertedFile = convertMultiPartToFile(file);
                builder.part("audio", new FileSystemResource(convertedFile))
                        .filename(Objects.requireNonNull(file.getOriginalFilename()));
            } catch (IOException e) {
                return Mono.error(new RuntimeException("파일 변환 중 오류 발생", e));
            }
        }
        return webClient.post()
                .uri("/register")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Server error: " + response.statusCode())))
                .bodyToMono(String.class);
    }

    @Override
    public File convertMultiPartToFile(MultipartFile file) throws IOException { // MultipartFile -> File로 변환해주는 헬퍼함수
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }
}
