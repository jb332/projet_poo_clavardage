package clavardage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CommunicationWindow implements ActionListener {
    private Clavardage chat;

    public CommunicationWindow(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

    public Component createComponents() {
        JButton[] users = new JButton[8];
        for(int i=0; i<8; i++) {
            users[i] = new JButton("User " + i);
        }

        JLabel[] messages = new JLabel[10];
        for(int i=0; i<10; i++) {
            messages[i] = new JLabel("Hello ! (" + i + ")");
        }

        JTextField writeZone = new JTextField();
        JButton sendButton = new JButton(">");
        JPanel sendZone = new JPanel(new GridLayout(1, 2));
        sendZone.add(writeZone);
        sendZone.add(sendButton);

        JPanel usersPane = new JPanel(new GridLayout(0, 1));
        GraphicalTools.addManyComponents(usersPane, users);

        JPanel messagesPane = new JPanel(new GridLayout(0, 1));
        GraphicalTools.addManyComponents(messagesPane, messages);
        messagesPane.add(sendZone);

        JPanel pane = new JPanel(new GridLayout(1, 2));
        pane.add(usersPane);
        pane.add(messagesPane);

        return pane;
    }
    public void displayThings() {
        //String theme = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        //GraphicalTools.setTheme(theme);

        JFrame frame = new JFrame("Tu veux-tu clavarder avec moi ?");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this.createComponents());
        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Un événement a eu lieu.");
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                displayThings();
            }
        });
    }

    public void notifyMessageReception(Message message) {
        // si l'utilisateur dont les messages sont actuellement affichés est l'émetteur du message passé en argument
        // alors on affiche ce message (on actualise la liste des messages)
        // sinon on peut afficher une notification sur le bouton correspondant à l'utilisateur émetteur
    }

    public void notifyMessageSent(Message message) {
        // TODO Auto-generated method stub

    }
}
