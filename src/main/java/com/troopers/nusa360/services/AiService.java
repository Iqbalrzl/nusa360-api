package com.troopers.nusa360.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public Flux<String> chat(String userQuery) {
        String systemMessageText = """
                  Anda adalah 'Maestro Budaya' dari platform Nusa360. Anda adalah avatar AI yang berpengetahuan luas tentang budaya, sejarah, dan filosofi Indonesia.\s
                            Jawablah semua pertanyaan dengan ramah, informatif, dan dalam konteks budaya Indonesia.\s
                            Jika memungkinkan, gunakan sapaan atau istilah budaya yang relevan.
                """;
        return chatClient
                .prompt()
                .system(systemMessageText)
                .user(userQuery)
                .stream()
                .content()
                .concatWith(Mono.just("[END_OF_STREAM]"));
    }

    public String chatWithContext(String userQuery, String context) {
        String contextLabuanBajo = """
                Nama anda adalah Jojo. Anda adalah 'Maestro Budaya' dari platform Nusa360. \s
                Anda adalah avatar AI yang berpengetahuan luas tentang budaya, sejarah, dan filosofi di Labuan Bajo, Indonesia.\s
                Jawablah semua pertanyaan dengan ramah, informatif, dan dalam konteks budaya Indonesia.\s
                Jika memungkinkan, gunakan sapaan atau istilah menggunakan bahasa budaya di Labuan Bajo, Indonesia.\s.
                """;
        String contextDanauToba = """
                Nama anda adalah Tara. Anda adalah 'Maestro Budaya' dari platform Nusa360. \s
                Anda adalah avatar AI yang berpengetahuan luas tentang budaya, sejarah, dan filosofi di Danau Toba, Indonesia.\s
                Jawablah semua pertanyaan dengan ramah, informatif, dan dalam konteks budaya Indonesia.\s
                Jika memungkinkan, gunakan sapaan atau istilah menggunakan bahasa budaya di Danau Toba, Indonesia.\s.
                """;
        return  chatClient.prompt()
                .system((context.equals("labuan-bajo")) ? contextLabuanBajo : contextDanauToba)
                .user(userQuery)
                .call()
                .content();
    }
}
