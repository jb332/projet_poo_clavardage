package gui;

import clavardage.Clavardage;
import clavardage.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChatWindow extends JFrame {
    private MessagesHistoryPanel messageHistoryPane;
    private UsersTabsPanel usersTabsPane;
    private SendZonePanel sendZonePane;
    private LoginPanel loginPane;

    public ChatWindow(ActionListener actionListener) {
        super("Clavardage");

        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.usersTabsPane = new UsersTabsPanel(actionListener);

        this.loginPane = new LoginPanel(actionListener);

        JPanel usersAndLoginPane = new JPanel(new BorderLayout());
        usersAndLoginPane.add(this.usersTabsPane);
        usersAndLoginPane.add(this.loginPane, BorderLayout.NORTH);

        //scroll pane for users at the left of the window
        JScrollPane usersScrollPane = new JScrollPane(usersAndLoginPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for messages history
        this.messageHistoryPane = new MessagesHistoryPanel();


        //scroll pane for messages history
        JScrollPane historyScrollPane = new JScrollPane(this.messageHistoryPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //pane for send zone (send button, text zone) at the bottom right of the window
        this.sendZonePane = new SendZonePanel(actionListener);


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
        super.setPreferredSize(new Dimension(600,350));
        this.sendZonePane.setSendKeyboardShorcut(this);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.getContentPane().add(mainPane, BorderLayout.CENTER);
        super.pack();
        super.setVisible(true);
    }

    public MessagesHistoryPanel getMessageHistoryPane() {
        return this.messageHistoryPane;
    }

    public UsersTabsPanel getUsersTabsPane() {
        return this.usersTabsPane;
    }

    public SendZonePanel getSendZonePane() {
        return this.sendZonePane;
    }

    public LoginPanel getLoginPane() {
        return this.loginPane;
    }
}
