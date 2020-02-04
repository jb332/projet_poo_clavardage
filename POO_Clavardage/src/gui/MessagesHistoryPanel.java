package gui;

import clavardage.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * The panel containing the messages exchanged with a user.
 */
public class MessagesHistoryPanel extends JPanel {
    /**
     * Constructor.
     */
    public MessagesHistoryPanel() {
        super(new GridBagLayout());
        super.setBorder(new EmptyBorder(10, 10, 10, 10));
        super.setPreferredSize(new Dimension(0, 0)); //magie noire
    }

    /**
     * Remove currently displayed messages and load a messages list
     * @param messages the messages you want to display
     */
    public void updateMessagesBubbles(ArrayList<Message> messages) {
        this.removeAll();

        if(messages != null) {
            for (Message currentMessage : messages) {
                MessageBubble currentMessageBubble = new MessageBubble(currentMessage);

                GridBagConstraints messagesBubblesConstraints = new GridBagConstraints();
                messagesBubblesConstraints.gridx = 0;
                messagesBubblesConstraints.gridy = GridBagConstraints.RELATIVE;
                messagesBubblesConstraints.weightx = 1;
                messagesBubblesConstraints.fill = GridBagConstraints.VERTICAL;
                if (currentMessage.isSent()) {
                    messagesBubblesConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
                } else {
                    messagesBubblesConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
                }
                if (currentMessage.equals(messages.get(messages.size() - 1))) {
                    messagesBubblesConstraints.weighty = 1;
                } else {
                    messagesBubblesConstraints.weighty = 0;
                }

                this.add(currentMessageBubble, messagesBubblesConstraints);
            }
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Display a message.
     * @param message the message you want to display
     */
    public void addMessageBubble(Message message) {
        if(this.getComponents().length != 0) {
            Integer lastMessageBubbleIndex = this.getComponents().length - 1;
            MessageBubble lastMessageBubble = (MessageBubble) (this.getComponent(lastMessageBubbleIndex));

            GridBagLayout layout = (GridBagLayout) (this.getLayout());
            GridBagConstraints lastMessageBubbleConstraints = layout.getConstraints(lastMessageBubble);
            lastMessageBubbleConstraints.weighty = 0;
            layout.setConstraints(lastMessageBubble, lastMessageBubbleConstraints);
        }

        MessageBubble messageBubble = new MessageBubble(message);

        GridBagConstraints messageBubbleConstraints = new GridBagConstraints();
        messageBubbleConstraints.gridx = 0;
        messageBubbleConstraints.gridy = GridBagConstraints.RELATIVE;
        messageBubbleConstraints.weightx = 1;
        messageBubbleConstraints.weighty = 1;
        messageBubbleConstraints.fill = GridBagConstraints.VERTICAL;
        if(message.isSent()){
            messageBubbleConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        } else {
            messageBubbleConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        }

        this.add(messageBubble, messageBubbleConstraints);

        this.revalidate();
        this.repaint();
    }
}
