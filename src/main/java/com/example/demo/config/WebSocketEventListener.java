package com.example.demo.config;

//import
import com.example.demo.chat.MessageType;
import com.example.demo.chat.ChatMessage;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j //for logging when user leaves chat
public class WebSocketEventListener {

    private final SimpMessagingTemplate messageTemplate;

    @EventListener //to listen on the given event
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        //TO DO inform users of the chat app that someone has left the chat
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("User Disconnected: " + username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend((String) "/topic/public", chatMessage);
        }
    }
}
