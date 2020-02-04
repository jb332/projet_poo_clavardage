package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The panel containing the sending text zone and the send button.
 */
public class SendZonePanel extends JPanel {
    /**
     * The text zone where messages are typed.
     */
    private JTextField sendTextZone;
    /**
     * The button to send messages.
     */
    private JButton sendButton;

    /**
     * Constructor.
     * @param actionListener an action listener. GUI plays this role since all events are supposed to be handled by it.
     */
    public SendZonePanel(ActionListener actionListener) {
        super(new BorderLayout());

        this.sendTextZone = new JTextField();
        this.sendButton = new JButton(">");
        this.sendButton.addActionListener(actionListener);

        this.add(this.sendTextZone);
        this.add(this.sendButton, BorderLayout.EAST);
    }

    /**
     * Disable the send zone.
     */
    public void disableSendZone() {
        this.sendTextZone.setEnabled(false);
        this.sendButton.setEnabled(false);
    }

    /**
     * Enable the send zone.
     */
    public void enableSendZone() {
        this.sendTextZone.setEnabled(true);
        this.sendButton.setEnabled(true);
    }

    /**
     * Get the typed message.
     * @return the typed message
     */
    public String getText() {
        return this.sendTextZone.getText();
    }

    /**
     * Clear the text zone.
     */
    public void clearTextZone() {
        this.sendTextZone.setText("");
    }

    /**
     * Set the send button as the default button for the chat window. It allows the local user to send messages pressing the "enter" key.
     * @param window the chat window
     */
    public void setSendKeyboardShorcut(JFrame window) {
        window.getRootPane().setDefaultButton(this.sendButton);
    }
}
