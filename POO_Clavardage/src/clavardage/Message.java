package clavardage;

import java.time.LocalDateTime;

public class Message {
    private LocalDateTime dateTime;
    private String content;
    private MessageWay way;
    /*
    private User sender;
    private User receiver;
    */
    public Message(String content, MessageWay way/*, User sender, User receiver*/) {
        this.dateTime = LocalDateTime.now();
        this.content = content;
        this.way = way;
        /*
        this.sender = sender;
        this.receiver = receiver;
        */
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

    /*
    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
   */
    public String toString() {
        return "content : " + this.getContent() + "\ndate and time : " + this.getDateTime() + "\n" + (this.isSent() ? "sent" : "received");
    }
}
