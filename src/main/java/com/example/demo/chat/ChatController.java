package com.example.demo.chat;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller //this is a controller
public class ChatController {
    //two methods, one for user and one for sending message
    //for a new user connecting to the chat application, tell everyone person1 joined the chat
    //method to send message
    @MessageMapping("/chat.sendMessage") //url to invoke send message
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
        @Payload ChatMessage chatMessage
    ){
        return chatMessage;
    }

    @MessageMapping("/chat.addUser") //url to invoke send message
    @SendTo("/topic/public")
    public ChatMesssage addUser(
            @Payload ChatMesssage chatMesssage,
            SimpMessageHeaderAccessor headerAccessor
            ){
        //add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMesssage.getSender());
        return chatMesssage;
    }
}
