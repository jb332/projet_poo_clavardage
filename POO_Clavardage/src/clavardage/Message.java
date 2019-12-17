package clavardage;

import java.time.LocalDateTime;

public class Message {
    private String content;
    private MessageWay way;
    private LocalDateTime dateTime;

    public Message(String content, MessageWay way) {
        this.content = content;
        this.way = way;
        this.dateTime = LocalDateTime.now();
    }

    public Message(String content, MessageWay way, LocalDateTime dateTime) {
        this.content = content;
        this.way = way;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getContent() {
        return this.content;
    }

    public MessageWay getMessageWay() {
        return this.way;
    }

    public boolean isSent() {
        return this.way == MessageWay.SENT;
    }

    public boolean isReceived() {
        return this.way == MessageWay.RECEIVED;
    }

    public String toString() {
        return "content : " + this.getContent() + "\ndate and time : " + this.getDateTime() + "\n" + (this.isSent() ? "sent" : "received");
    }
}
