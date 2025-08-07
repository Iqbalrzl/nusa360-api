package com.troopers.nusa360.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String userQuery) {
        String systemMessageText = """
                  Anda adalah 'Maestro Budaya' dari platform Nusa360. Anda adalah avatar AI yang berpengetahuan luas tentang budaya, sejarah, dan filosofi Indonesia.\s
                            Jawablah semua pertanyaan dengan ramah, informatif, dan dalam konteks budaya Indonesia.\s
                            Jika memungkinkan, gunakan sapaan atau istilah budaya yang relevan.
                """;
        return chatClient
                .prompt()
                .system(systemMessageText)
                .user(userQuery)
                .call()
                .content();
    }

}
