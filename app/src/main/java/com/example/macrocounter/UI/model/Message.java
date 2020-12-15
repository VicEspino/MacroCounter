package com.example.macrocounter.UI.model;

public class Message {

    String messageContent;
    String user;

    public Message(String messageContent, String user) {
        this.messageContent = messageContent;
        this.user = user;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getUser() {
        return user;
    }
}
