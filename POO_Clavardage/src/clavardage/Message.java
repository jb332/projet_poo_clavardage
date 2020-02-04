package clavardage;

import java.time.LocalDateTime;

/**
 * A class representing a message.
 */
public class Message {
    /**
     * The content of the message.
     */
    private String content;
    /**
     * Indicate if the message was sent or received.
     */
    private boolean isSent;
    /**
     * Date and time when the message was received/sent.
     */
    private LocalDateTime dateTime;

    /**
     * Can be passed as a second argument in the constructor to indicate the message was sent.
     */
    public final static boolean SENT = true;
    /**
     * Can be passed as a second argument in the constructor to indicate the message was received.
     */
    public final static boolean RECEIVED = false;

    /**
     * Constructor that sets the current date and time.
     * @param content the content of the message
     * @param way specifies if the message was sent or received
     */
    public Message(String content, boolean way) {
        this.content = content;
        this.isSent = way;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Constructor that sets the specified date and time.
     * @param content the content of the message
     * @param way specifies if the message was sent or received
     * @param dateTime the date and time the message was sent/received
     */
    public Message(String content, boolean way, LocalDateTime dateTime) {
        this.content = content;
        this.isSent = way;
        this.dateTime = dateTime;
    }

    /**
     * Get the date and time the message was sent or received.
     * @return the date and time the message was sent or received
     */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Get the content of the message.
     * @return the content of the message
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Tell if the message was sent.
     * @return true if the message was sent, false if it was received
     */
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
