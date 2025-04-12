package com.example.demo.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMesssage {
    private String content;
    private String sender;
    private MessageType type;

}


