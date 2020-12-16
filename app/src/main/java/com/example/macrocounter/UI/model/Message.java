package com.example.macrocounter.UI.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    /*@ServerTimestamp */private Date messageTime;
    private String messageContent;
    private String user;
    private static SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");

    public Message(String user, String messageContent, Date messageTime) {
        this.messageContent = messageContent;
        this.user = user;
        this.messageTime = messageTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getUser() {
        return user;
    }

    public Date getMessageFullDate() {
        return messageTime;
    }

    public String getMessageTime(){

        String time = formatTime.format(this.messageTime);

       return time;
    }

}
