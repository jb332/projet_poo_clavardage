package clavardage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CommunicationWindow implements ActionListener {
    private Clavardage chat;

    private JPanel historyPane;
    private JPanel usersPane;
    private JPanel sendPane;
    private JFrame frame;

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

        //System.out.println(this.historyPane.getComponents());

        for(Message currentMessage : messages) {
            MessageBubble currentMessageBubble = new MessageBubble(currentMessage);
            this.historyPane.add(currentMessageBubble);
        }

        this.historyPane.revalidate();
        this.historyPane.repaint();

        //System.out.println(this.messagesPane.getComponents());
    }

    /*
    private Component createComponents() {
        ArrayList<User> users = this.chat.getUsers();
        this.usersPane = new JPanel(new GridLayout(0, 1));

        for(User currentUser : users) {
            UserButton currentUserButton = new UserButton(currentUser);
            currentUserButton.addActionListener(this);
            this.usersPane.add(currentUserButton);
        }

        this.messagesPane = new JPanel(new GridLayout(0, 1));
        this.sendZone = createSendZone();

        User arbitraryUserWhoseMessagesAreDisplayedFirst = users.get(0);
        loadMessages(arbitraryUserWhoseMessagesAreDisplayedFirst);

        JPanel pane = new JPanel(new GridLayout(1, 2));
        pane.add(this.usersPane);
        pane.add(this.messagesPane);

        return pane;
    }
    */

    private void initializeGUI() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //pane for users (save)
        /*
        ArrayList<User> users = this.chat.getUsers();
        this.usersPane = new JPanel();

        this.usersPane.setLayout(new BoxLayout(usersPane, BoxLayout.Y_AXIS));

        for(User currentUser : users) {
            UserButton currentUserButton = new UserButton(currentUser);

            currentUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            //currentUserButton.setPreferredSize(new Dimension(500, 40));
            currentUserButton.addActionListener(this);

            currentUserButton.setMinimumSize(new Dimension(500, 40));

            this.usersPane.add(currentUserButton);
        }
        */

        //pane for users
        ArrayList<User> users = this.chat.getUsers();
        this.usersPane = new JPanel(new GridLayout(0, 1));

        for(User currentUser : users) {
            UserButton currentUserButton = new UserButton(currentUser);

            //currentUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentUserButton.addActionListener(this);

            this.usersPane.add(currentUserButton);
        }

        JPanel usersCenterPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1;
        c.weighty = 0.2;
        usersCenterPane.add(this.usersPane, c);


        /*
        JPanel usersPanel = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < 6; i++) {
            buttonPanel.add(new JButton("Button " + i));
        }
        buttonPanel.add(new JButton("Truc"));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        buttonCenter.add(buttonPanel, c);
        */




        //scroll pane for users at the left of the window
        //SAVE : JScrollPane usersScrollPane = new JScrollPane(this.usersPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane usersScrollPane = new JScrollPane(usersCenterPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for messages history
        this.historyPane = new JPanel();
        User arbitraryUserWhoseMessagesAreDisplayedFirst = users.get(0);
        loadMessages(arbitraryUserWhoseMessagesAreDisplayedFirst);


        //scroll pane for messages history
        JScrollPane historyScrollPane = new JScrollPane(this.historyPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        //pane for send zone at the bottom right of the window
        JTextField sendTextZone = new JTextField();
        JButton sendButton = new JButton(">");
        this.sendPane = new JPanel();
        this.sendPane.setLayout(new BorderLayout());
        this.sendPane.add(sendTextZone);
        this.sendPane.add(sendButton, BorderLayout.EAST);


        //pane for messages at the right of the window
        JPanel messagesPane = new JPanel();
        messagesPane.setLayout(new BorderLayout());
        messagesPane.add(historyScrollPane);
        messagesPane.add(this.sendPane, BorderLayout.SOUTH);


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

    @Override
    public void actionPerformed(ActionEvent event) {
        String selectedUserLogin = event.getActionCommand();
        User selectedUser = this.chat.getUserFromLogin(selectedUserLogin);
        loadMessages(selectedUser);
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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
