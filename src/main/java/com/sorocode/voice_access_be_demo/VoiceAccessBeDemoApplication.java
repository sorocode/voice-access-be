package com.sorocode.voice_access_be_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VoiceAccessBeDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceAccessBeDemoApplication.class, args);
    }

}
