package com.example.demo.chat;

public interface MessageProcessor {
    // This method will be called when a message is received
    void processMessage(AbstractMessage message);
}

