package clavardage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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

        this.historyPane.removeAll();

        for(Message currentMessage : messages) {
            JLabel currentMessageBubble = new JLabel(currentMessage.getContent());
            currentMessageBubble.setVerticalAlignment(JLabel.TOP);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = GridBagConstraints.RELATIVE;
            c.weightx = 1;
            c.fill = GridBagConstraints.VERTICAL;

            if(currentMessage.isSent()) {
                c.anchor = GridBagConstraints.FIRST_LINE_START;
            } else {
                c.anchor = GridBagConstraints.FIRST_LINE_END;
            }

            if(currentMessage.equals(messages.get(messages.size()-1))) {
                c.weighty = 1;
            } else {
                c.weighty = 0;
            }
            this.historyPane.add(currentMessageBubble, c);
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
        this.usersPane = new JPanel(new GridLayout(0, 1));

        ArrayList<JButton> userButtons = this.chat.getUsers().generateUserButtons();
        for(JButton currentUserButton : userButtons) {
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
        this.historyPane = new JPanel(new /*GridLayout(0, 1)*/GridBagLayout());
        this.historyPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        this.historyPane.setPreferredSize(new Dimension(400, 200));

        /*
        JPanel messagesCenterPane = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.anchor = GridBagConstraints.NORTHWEST;
        c2.weightx = 1;
        c2.weighty = 0.2;
        */

        //messagesCenterPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        /*
        messagesCenterPane.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        messagesCenterPane.add(this.historyPane, c2);
        */

        this.selectedUser = this.chat.getUsers().getArbitraryUser();
        loadMessages(selectedUser);


        //scroll pane for messages history
        JScrollPane historyScrollPane = new JScrollPane(this.historyPane/*messagesCenterPane*/, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


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
        this.frame.setPreferredSize(new Dimension(600,350));

        //this.frame.setBounds(100, 100, 450, 300);
        this.frame.getRootPane().setDefaultButton(sendButton);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().add(mainPane, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void sendMessage(Message sentMessage, User receiver) {
        //send the message over the network and store it
        this.chat.sendMessage(sentMessage, receiver);

        //display the message in history
        this.addMessage(sentMessage);

        //remove the text in the sendTextZone
        this.sendTextZone.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonName = event.getActionCommand();
        if(buttonName.equals(">")) {
            String content = this.sendTextZone.getText();
            if(!content.equals("")) {
                Message messageSent = new Message(content, MessageWay.SENT);
                sendMessage(messageSent, this.selectedUser);
            }
        } else {
            User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
            loadMessages(selectedUser);
        }
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();
            }
        });
    }

    public void addMessage(Message message) {
        Integer lastMessageBubbleIndex = this.historyPane.getComponents().length-1;
        JLabel lastMessageBubble = (JLabel)(this.historyPane.getComponent(lastMessageBubbleIndex));

        GridBagLayout layout = (GridBagLayout)(this.historyPane.getLayout());
        GridBagConstraints lastMessageBubbleConstraints = layout.getConstraints(lastMessageBubble);
        lastMessageBubbleConstraints.weighty = 0;
        layout.setConstraints(lastMessageBubble, lastMessageBubbleConstraints);

        JLabel messageBubble = new JLabel(message.getContent());
        messageBubble.setVerticalAlignment(JLabel.TOP);

        GridBagConstraints messageBubbleConstraints = new GridBagConstraints();
        messageBubbleConstraints.gridx = 0;
        messageBubbleConstraints.gridy = GridBagConstraints.RELATIVE;
        messageBubbleConstraints.weightx = 1;
        messageBubbleConstraints.fill = GridBagConstraints.VERTICAL;
        if(message.isSent()){
            messageBubbleConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        } else {
            messageBubbleConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        }
        messageBubbleConstraints.weighty = 1;

        this.historyPane.add(messageBubble, messageBubbleConstraints);
        this.historyPane.revalidate();
        this.historyPane.repaint();
    }

    public void notifyMessageReception(Message receivedMessage, User sender) {
        if(sender.equals(this.selectedUser)){
            this.addMessage(receivedMessage);
        } else {
            //display a notification on the sender's JButton
        }
    }

    public void addUser(User user) {
        JButton userButton = new JButton(user.getLogin());
        userButton.addActionListener(this);
        this.usersPane.add(userButton);
        this.usersPane.revalidate();
        this.usersPane.repaint();
    }
}
