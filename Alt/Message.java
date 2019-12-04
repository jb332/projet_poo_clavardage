package clavardage;

import java.time.LocalDateTime;

public class Message {
    LocalDateTime dateTime;
    String content;
    User sender;
    User receiver;

    public Message(String content, User sender, User receiver) {
        this.dateTime = LocalDateTime.now();
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }
}
