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

//Controller marks this class as a Spring MVC controller
@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    //defines the destination for sending messages
    private static final String PUBLIC_TOPIC = "/topic/public";
    //servce to handle chat messages
    private final ChatMessageService messageService;
    //aurowired auto injects the service
    @Autowired
    public ChatController(ChatMessageService messageService) {
        this.messageService = messageService;
    }
    //maps messages sent to /app/chat.sendMessage.
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
    //adds a user to the chat by storing the username in the session attributes
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