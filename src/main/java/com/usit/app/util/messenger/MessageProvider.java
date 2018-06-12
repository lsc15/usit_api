package com.usit.app.util.messenger;

public class MessageProvider {

    public MessageSender getSender() {
        return sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    private MessageSender sender;

}
