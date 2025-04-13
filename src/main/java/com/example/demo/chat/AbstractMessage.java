package com.example.demo.chat;

public abstract class AbstractMessage {
    private String sender;

    // Default constructor needed by Lombok-generated constructors in subclasses
    public AbstractMessage() {
    }

    public AbstractMessage(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public abstract MessageType getType();
}
