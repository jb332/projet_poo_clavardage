package gui;

import clavardage.Message;

import javax.swing.*;

public class MessageBubble extends JLabel {
    public MessageBubble(Message message) {
        super(message.getContent());
        super.setVerticalAlignment(JLabel.TOP);
    }
}
