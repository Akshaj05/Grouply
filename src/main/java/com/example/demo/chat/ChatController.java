package com.example.demo.chat;
import com.example.demo.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private static final String PUBLIC_TOPIC = "/topic/public";

    private final ChatMessageService messageService;

    @Autowired
    public ChatController(ChatMessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo(PUBLIC_TOPIC)
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage == null) {
            throw new IllegalArgumentException("ChatMessage must not be null");
        }

        messageService.processMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo(PUBLIC_TOPIC)
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        if (chatMessage == null || chatMessage.getSender() == null) {
            throw new IllegalArgumentException("ChatMessage or sender must not be null");
        }

        if (!headerAccessor.getSessionAttributes().containsKey("username")) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }

        messageService.processMessage(chatMessage);
        return chatMessage;
    }
}