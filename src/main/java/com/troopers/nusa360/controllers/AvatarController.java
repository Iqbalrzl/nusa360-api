package com.troopers.nusa360.controllers;

import com.troopers.nusa360.services.AiService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class AvatarController {

    private final AiService aiService;

    @MessageMapping("/askAvatar")
    @SendTo("/topic/responses")
    public String handleUserQuery(String query) throws Exception {

        System.out.println("Receiving Questions: " + query);
//        Thread.sleep(1000);

        String aiReply = aiService.chat(query);

        String response = aiService.chat(query);

        return response;
    }
}
