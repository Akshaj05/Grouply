package com.example.demo.chat;
import com.example.demo.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private static final String PUBLIC_TOPIC = "/topic/public";

    // Method to send a message
    @MessageMapping("/chat.sendMessage") // URL to invoke this method for sending a message
    @SendTo(PUBLIC_TOPIC) // Sends the message to the public topic
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage == null) {
            throw new IllegalArgumentException("ChatMessage must not be null");
        }

        logger.info("Message received: {}", chatMessage);
        return chatMessage;
    }

    // Method for adding a user to the chat application
    @MessageMapping("/chat.addUser") // URL to invoke this method for adding a new user
    @SendTo(PUBLIC_TOPIC) // Sends a notification to the public topic
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        if (chatMessage == null || chatMessage.getSender() == null) {
            throw new IllegalArgumentException("ChatMessage or sender must not be null");
        }

        // Check if the username is already set in the session
        if (!headerAccessor.getSessionAttributes().containsKey("username")) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        } else {
            logger.warn("Username is already set in the session: {}",
                    headerAccessor.getSessionAttributes().get("username"));
        }

        logger.info("User joined: {}", chatMessage.getSender());
        return chatMessage;
    }
}