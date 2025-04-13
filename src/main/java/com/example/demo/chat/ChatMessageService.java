package com.example.demo.chat;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Getter
//Service is a Spring annotation i.e. it is used to define an object as a service
@Service
public class ChatMessageService implements MessageProcessor {
    //logger is used to log messages
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);
    //a list to store the history of messages
    private final List<AbstractMessage> messageHistory = new ArrayList<>();
    //a list to store the active users
    private final List<String> activeUsers = new ArrayList<>();

    @Override
    public void processMessage(AbstractMessage message) {
        // checking if the message is a JOIN or LEAVE message and update active users accordingly
        if (message.getType() == MessageType.JOIN && !activeUsers.contains(message.getSender())) {
            activeUsers.add(message.getSender());
        } else if (message.getType() == MessageType.LEAVE) {
            activeUsers.remove(message.getSender());
        }
        // adding the message to the history
        messageHistory.add(message);
        logger.info("Processed message: {}", message);
    }

}
