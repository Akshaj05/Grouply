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
    //Setter is useful when we need to modify the sender after object creation
    public void setSender(String sender) {
        this.sender = sender;
    }

    public abstract MessageType getType();
}
