package clavardage;

import java.time.LocalDateTime;

public class Message {
    private String content;
    private boolean isSent;
    private LocalDateTime dateTime;

    public final static boolean SENT = true;
    public final static boolean RECEIVED = false;

    public Message(String content, boolean way) {
        this.content = content;
        this.isSent = way;
        this.dateTime = LocalDateTime.now();
    }

    public Message(String content, boolean way, LocalDateTime dateTime) {
        this.content = content;
        this.isSent = way;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getContent() {
        return this.content;
    }

    public boolean isSent() {
        return this.isSent;
    }

    public String toString() {
        return
            "content : " + this.getContent() + "\n" +
            "date and time : " + this.getDateTime() + "\n" +
            (this.isSent() ? "sent" : "received");
    }
}
