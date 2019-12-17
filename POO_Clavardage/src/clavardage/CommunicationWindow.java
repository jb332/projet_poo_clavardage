package clavardage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CommunicationWindow implements ActionListener {
    private Clavardage chat;

    private JPanel historyPane;
    private JPanel usersPane;
    private JFrame frame;
    private JTextField sendTextZone;

    private User selectedUser;

    public CommunicationWindow(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

    private JPanel createSendZone() {
        JTextField writeZone = new JTextField();
        JButton sendButton = new JButton(">");
        JPanel sendZone = new JPanel(new GridLayout(1, 2));
        sendZone.add(writeZone);
        sendZone.add(sendButton);
        return sendZone;
    }

    //changes the message displayed upon selecting another user
    private void loadMessages(User selectedUser) {
        ArrayList<Message> messages = this.chat.getMessages(selectedUser);

        //TEST
        Integer i = 0;
        for(Message currentMessage : messages) {
            System.out.println("Message" + i);
            System.out.println(currentMessage);
            i++;
        }
        //FIN TEST

        this.historyPane.removeAll();

        for(Message currentMessage : messages) {
            MessageBubble currentMessageBubble = new MessageBubble(currentMessage);
            this.historyPane.add(currentMessageBubble);
        }

        this.historyPane.revalidate();
        this.historyPane.repaint();
    }

    private void initializeGUI() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //pane for users
        ArrayList<User> users = this.chat.getUsers();
        this.usersPane = new JPanel(new GridLayout(0, 1));

        for(User currentUser : users) {
            UserButton currentUserButton = new UserButton(currentUser);
            currentUserButton.addActionListener(this);

            this.usersPane.add(currentUserButton);
        }

        JPanel usersCenterPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1;
        c.weighty = 0.2;
        usersCenterPane.add(this.usersPane, c);

        //scroll pane for users at the left of the window
        JScrollPane usersScrollPane = new JScrollPane(usersCenterPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for messages history
        this.historyPane = new JPanel();
        this.selectedUser = users.get(0);
        loadMessages(selectedUser);


        //scroll pane for messages history
        JScrollPane historyScrollPane = new JScrollPane(this.historyPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        //pane for send zone at the bottom right of the window
        this.sendTextZone = new JTextField();
        JButton sendButton = new JButton(">");
        sendButton.addActionListener(this);
        JPanel sendPane = new JPanel();
        sendPane.setLayout(new BorderLayout());
        sendPane.add(this.sendTextZone);
        sendPane.add(sendButton, BorderLayout.EAST);


        //pane for messages at the right of the window
        JPanel messagesPane = new JPanel();
        messagesPane.setLayout(new BorderLayout());
        messagesPane.add(historyScrollPane);
        messagesPane.add(sendPane, BorderLayout.SOUTH);


        //main panel (split panel with a divider)
        JSplitPane mainPane = new JSplitPane();
        mainPane.setDividerLocation(150);
        mainPane.setLeftComponent(usersScrollPane);
        mainPane.setRightComponent(messagesPane);


        //window
        this.frame = new JFrame("Tu veux-tu clavarder avec moi ?");
        this.frame.setSize(300,800);

        this.frame.setBounds(100, 100, 450, 300);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().add(mainPane, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void sendMessage(Message messageSent, User receiver) {
        //send the message over the network
        this.chat.sendMessage(messageSent, receiver);

        //store the message in the database
        this.chat.storeSentMessage(messageSent, receiver);

        //display the message in history
        MessageBubble messageSentBubble = new MessageBubble(messageSent);
        this.historyPane.add(messageSentBubble);
        this.historyPane.revalidate();
        this.historyPane.repaint();

        //remove the text in the sendTextZone
        this.sendTextZone.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonName = event.getActionCommand();
        if(buttonName.equals(">")) {
            Message messageSent = new Message(this.sendTextZone.getText(), MessageWay.SENT);
            sendMessage(messageSent, this.selectedUser);
        } else {
            User selectedUser = this.chat.getUserFromLogin(buttonName);
            loadMessages(selectedUser);
        }
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();
                initializeGUI();
            }
        });
    }

    public void notifyMessageReception(Message message, User sender) {
        // si l'utilisateur dont les messages sont actuellement affichés est l'émetteur du message passé en argument
        // alors on affiche ce message (on actualise la liste des messages)
        // sinon on peut afficher une notification sur le bouton correspondant à l'utilisateur émetteur
    }

    public void notifyMessageSent(Message message, User receiver) {
        // TODO Auto-generated method stub

    }
}
