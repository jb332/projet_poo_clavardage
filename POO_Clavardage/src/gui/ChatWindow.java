package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main window where you can chat.
 */
public class ChatWindow extends JFrame {
    /**
     * The panel containing the messages exchanged with a user.
     */
    private MessagesHistoryPanel messageHistoryPane;
    /**
     * The panel containing the users you can select.
     */
    private UsersTabsPanel usersTabsPane;
    /**
     * The panel containing the sending text zone and the send button.
     */
    private SendZonePanel sendZonePane;
    /**
     * The panel containing the user login and the change login button.
     */
    private LoginPanel loginPane;

    /**
     * Constructor.
     * @param actionListener an action listener. GUI plays this role since all events are supposed to be handled by it.
     */
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

    /**
     * Get the message history panel.
     * @return the message history panel
     */
    public MessagesHistoryPanel getMessageHistoryPane() {
        return this.messageHistoryPane;
    }

    /**
     * Get the users tabs panel.
     * @return the users tabs panel
     */
    public UsersTabsPanel getUsersTabsPane() {
        return this.usersTabsPane;
    }

    /**
     * Get the send zone panel.
     * @return the send zone panel.
     */
    public SendZonePanel getSendZonePane() {
        return this.sendZonePane;
    }

    /**
     * Get the login panel.
     * @return the login panel
     */
    public LoginPanel getLoginPane() {
        return this.loginPane;
    }
}
