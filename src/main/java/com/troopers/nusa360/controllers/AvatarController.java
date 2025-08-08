package com.troopers.nusa360.controllers;

import com.troopers.nusa360.dtos.AskAiRequest;
import com.troopers.nusa360.services.AiService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Controller
public class AvatarController {

    private final AiService aiService;
    private final SimpMessagingTemplate messagingTemplate;

    // --Websocket--
    @MessageMapping("/askAvatar")
    public void handleUserQuery(String query) {
        System.out.println("Receiving Questions: " + query);

        Flux<String> aiResponseStream = aiService.chat(query);

        aiResponseStream.subscribe(response -> {
            messagingTemplate.convertAndSend("/topic/responses", response);
        });
    }

    // --RestAPI--
    @PostMapping("/askAi")
    public ResponseEntity<String> handleAskAi(
            @Valid @RequestBody AskAiRequest request
    ){
        return ResponseEntity.ok(aiService.chatWithContext(request.getQuery(), request.getContext()));
    }
}
