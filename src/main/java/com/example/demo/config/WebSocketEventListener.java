package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j //for logging when user leaves chat
public class WebSocketEventListener {
    @EventListener //to listen on the given event
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        //TO DO inform users of the chat app that someone has left the chat
    }
}
