package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker

//this class allows us to customize the WebSocket configuration
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    //this method enables user to connect to the WebSocket server
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // /ws is websocket endpoint url that the client will use to connect to the server
        registry.addEndpoint("/ws").withSockJS();
    }
    @Override
    //this method allows us to configure the message broker
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
