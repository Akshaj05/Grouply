package com.example.demo.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
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


