package com.example.demo.chat;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Getter
@Service
public class ChatMessageService implements MessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final List<AbstractMessage> messageHistory = new ArrayList<>();
    private final List<String> activeUsers = new ArrayList<>();

    @Override
    public void processMessage(AbstractMessage message) {
        if (message.getType() == MessageType.JOIN && !activeUsers.contains(message.getSender())) {
            activeUsers.add(message.getSender());
        } else if (message.getType() == MessageType.LEAVE) {
            activeUsers.remove(message.getSender());
        }

        messageHistory.add(message);
        logger.info("Processed message: {}", message);
    }

}
