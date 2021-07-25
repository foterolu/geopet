package com.example.geopet.ChatMessage;

import java.util.Date;

public class Chats {

    private String chatId;

    public Chats(String chatId) {
        this.chatId = chatId;

        // Initialize to current time
    }

    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }


}
