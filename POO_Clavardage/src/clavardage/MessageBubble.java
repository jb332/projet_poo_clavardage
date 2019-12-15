package clavardage;

import javax.swing.*;

public class MessageBubble extends JLabel {
    private Message message;

    public MessageBubble(Message message) {
        super(message.getContent());
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }
}
