package gui;

import clavardage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CommunicationWindow implements ActionListener {
    private Clavardage chat;

    private MessagesHistoryPanel messageHistoryPane;
    private UsersTabsPanel usersTabsPane;
    private SendZonePanel sendZonePane;
    private JFrame frame;
    //private JTextField sendTextZone;

    private User selectedUser;

    public CommunicationWindow(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

    //changes the message displayed upon selecting another user
    private void changeUserAndUpdateMessages(User selectedUser) {
        this.selectedUser = selectedUser;
        this.usersTabsPane.removeNotificationFromUserTab(selectedUser);

        ArrayList<Message> messages = this.chat.getMessages(selectedUser);
        this.messageHistoryPane.updateMessagesBubbles(messages);
    }

    private void initializeGUI() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.usersTabsPane = new UsersTabsPanel(this);
        this.usersTabsPane.loadUsers(this.chat.getUsers().getList());

        //scroll pane for users at the left of the window
        JScrollPane usersScrollPane = new JScrollPane(this.usersTabsPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for messages history
        this.messageHistoryPane = new MessagesHistoryPanel();

        User selectedUser = this.chat.getUsers().getArbitraryUser();
        this.changeUserAndUpdateMessages(selectedUser);
        this.usersTabsPane.setUserTabToOnline(selectedUser);


        //scroll pane for messages history
        JScrollPane historyScrollPane = new JScrollPane(this.messageHistoryPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for send zone (send button, text zone) at the bottom right of the window
        this.sendZonePane = new SendZonePanel(this);

        //pane for messages (send zone, messages history) at the right of the window
        JPanel messagesPane = new JPanel();
        messagesPane.setLayout(new BorderLayout());
        messagesPane.add(historyScrollPane);
        messagesPane.add(this.sendZonePane, BorderLayout.SOUTH);


        //main panel (split panel with a divider)
        JSplitPane mainPane = new JSplitPane();
        mainPane.setDividerLocation(150);
        mainPane.setLeftComponent(usersScrollPane);
        mainPane.setRightComponent(messagesPane);


        //window
        this.frame = new JFrame("Tu veux-tu clavarder avec moi ?");
        this.frame.setPreferredSize(new Dimension(600,350));
        //this.frame.getRootPane().setDefaultButton(sendButton);
        this.sendZonePane.setSendKeyboardShorcut(this.frame);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().add(mainPane, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void sendMessage(Message sentMessage, User receiver) {
        //send the message over the network and store it
        this.chat.sendAndStoreMessage(sentMessage, receiver);

        //display the message in history
        this.messageHistoryPane.addMessageBubble(sentMessage);

        //remove the text in the sendTextZone
        this.sendZonePane.clearTextZone();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonName = event.getActionCommand();
        if(buttonName.equals(">")) {
            String content = this.sendZonePane.getText();
            if(!content.equals("")) {
                Message messageSent = new Message(content, MessageWay.SENT);
                this.sendMessage(messageSent, this.selectedUser);
            }
        } else {
            User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
            changeUserAndUpdateMessages(selectedUser);
        }
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();
            }
        });
    }

    public void notifyMessageReception(Message receivedMessage, User sender) {
        if(sender.equals(this.selectedUser)){
            this.messageHistoryPane.addMessageBubble(receivedMessage);
        } else {
            this.usersTabsPane.addNotificationToUserTab(sender);
        }
    }

    public void addUser(User user) {
        JButton userTab = new JButton(user.getLogin());
        userTab.addActionListener(this);
        this.usersTabsPane.add(userTab);

        this.usersTabsPane.revalidate();
        this.usersTabsPane.repaint();
    }
}
