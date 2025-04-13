package com.example.demo.chat;

import lombok.*;

//auto generate getters and setters for all fields
@Getter
@Setter
@NoArgsConstructor
//enable builder pattern for this class
@Builder
public class ChatMessage extends AbstractMessage {
    private String content;
    private String sender;
    private MessageType type;

    public ChatMessage(String sender, String content, MessageType type) {
        super(sender);
        this.content = content;
        this.type = type;
    }

    @Override
    public MessageType getType() {
        return this.type;
    }
}


