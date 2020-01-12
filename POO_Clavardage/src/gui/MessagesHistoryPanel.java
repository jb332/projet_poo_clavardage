package gui;

import clavardage.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MessagesHistoryPanel extends JPanel {

    public MessagesHistoryPanel() {
        super(new GridBagLayout());
        super.setBorder(new EmptyBorder(10, 10, 10, 10));
        super.setPreferredSize(new Dimension(0, 0)); //magie noire
    }

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
                //on met une weighty de 0 pour tous les Components du JPanel sauf le dernier de manière à ce qu'ils prennent la place dont ils ont besoin uniquement
                //on met une weighty de 1 au dernier Component pour qu'il prenne toute la place restante (si on ne fait pas ça, les cellules du JPanel se centrent et les messages apparaissent au centre au lieu d'apparaître en haut
                //de manière à ce que le texte du dernier Component s'affiche à la suite des autres Component, on a indiqué plus haut (JLabel.TOP) aux Components d'afficher le texte en haut
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

    public void addMessageBubble(Message message) {
        //s'il y a au moins un message dans le Panel, on règle la weighty du dernier message à 0 et on ajoute notre message avec une weighty de 1
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
