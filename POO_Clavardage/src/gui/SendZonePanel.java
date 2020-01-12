package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SendZonePanel extends JPanel {
    private JTextField sendTextZone;
    private JButton sendButton;

    public SendZonePanel(ActionListener actionListener) {
        super(new BorderLayout());

        this.sendTextZone = new JTextField();
        this.sendButton = new JButton(">");
        this.sendButton.addActionListener(actionListener);

        this.add(this.sendTextZone);
        this.add(this.sendButton, BorderLayout.EAST);
    }

    public void disableSendZone() {
        this.sendTextZone.setEnabled(false);
        this.sendButton.setEnabled(false);
    }

    public void enableSendZone() {
        this.sendTextZone.setEnabled(true);
        this.sendButton.setEnabled(true);
    }

    public String getText() {
        return this.sendTextZone.getText();
    }

    public void clearTextZone() {
        this.sendTextZone.setText("");
    }

    public void setSendKeyboardShorcut(JFrame window) {
        window.getRootPane().setDefaultButton(this.sendButton);
    }

}
