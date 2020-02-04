package gui;

import clavardage.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * View. The graphical interface.
 */
public class GUI implements ActionListener {
    /**
     * Used to ensure single instantiation.
     */
    private static boolean instantiated = false;

    /**
     * The controller.
     */
    private Clavardage chat;

    /**
     * The main chat window.
     */
    private ChatWindow chatWindow;
    /**
     * The login window.
     */
    private LoginWindow loginWindow;

    /**
     * The currently selected user.
     */
    private User selectedUser;

    /**
     * Constructor.
     * @param chat the controller
     */
    private GUI(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

    /**
     * Create an instance of the class. It raises an error if called twice.
     * @param chat the controller
     * @return an instance of the class
     */
    public static synchronized GUI instantiate(Clavardage chat) {
        GUI gui = null;
        if(!GUI.instantiated) {
            GUI.instantiated = true;
            gui = new GUI(chat);
        } else {
            System.out.println("Fatal error : GUI can not be instantiated twice");
            System.exit(1);
        }
        return gui;
    }

    /**
     * Select another user and update the displayed exchanged messages. It removes the potential notification from the selected user and enables or disables the send zone depending on the user status.
     * @param selectedUser the user you want to select
     */
    private void changeSelectedUserAndUpdateMessages(User selectedUser) {
        if(this.selectedUser != null) {
            this.chatWindow.getUsersTabsPane().getUserTab(this.selectedUser).unSelect();
        }
        this.selectedUser = selectedUser;

        if(selectedUser != null) {
            UserTab selectedUserTab = this.chatWindow.getUsersTabsPane().getUserTab(selectedUser);
            selectedUserTab.removeNotification();
            selectedUserTab.select();

            ArrayList<Message> messages = this.chat.getMessages(selectedUser);
            this.chatWindow.getMessageHistoryPane().updateMessagesBubbles(messages);

            if(selectedUser.isConnected()) {
                this.chatWindow.getSendZonePane().enableSendZone();
            } else {
                this.chatWindow.getSendZonePane().disableSendZone();
            }
        } else {
            this.chatWindow.getSendZonePane().disableSendZone();
        }
    }

    /**
     * Launch the main chat window. Called when the local user has picked a login.
     */
    private void launchChatWindow() {
        this.chatWindow = new ChatWindow(this);

        this.chatWindow.getUsersTabsPane().loadUsers(this.chat.getUsers().getList());
        //selecting a random user to start
        User selectedUser = this.chat.getUsers().getArbitraryUser();
        this.changeSelectedUserAndUpdateMessages(selectedUser);
    }

    /**
     * Send a message to a distant user.
     * @param sentMessage the message you want to send
     * @param receiver the receiver
     */
    public void sendMessage(Message sentMessage, User receiver) {
        //send the message over the network and store it
        this.chat.sendAndStoreMessage(sentMessage, receiver);

        //display the message in history
        this.chatWindow.getMessageHistoryPane().addMessageBubble(sentMessage);

        //remove the text in the sendTextZone
        this.chatWindow.getSendZonePane().clearTextZone();
    }

    /**
     * Called upon a button click event.
     * @param event an event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonName = event.getActionCommand();

        switch(buttonName) {
            case ">":
                String content = this.chatWindow.getSendZonePane().getText();
                if(!content.equals("")) {
                    Message messageSent = new Message(content, Message.SENT);
                    this.sendMessage(messageSent, this.selectedUser);
                }
                break;
            case "OK":
                this.loginWindow.setVisible(false);

                String login = this.loginWindow.getLogin();
                this.chat.setMyLogin(login);

                if(this.chatWindow == null) {
                    launchChatWindow();
                } else {
                    this.chatWindow.setVisible(true);
                }

                this.chatWindow.getLoginPane().changeMyLogin(login);
                this.chat.connect(login);
                break;
            case "Edit":
                this.switchToLoginWindow(true);
                break;
            default:
                User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
                changeSelectedUserAndUpdateMessages(selectedUser);
        }
    }

    /**
     * Switch from the chat window to the login window.
     * @param isLoginChangeVoluntary true if the login change if voluntary or subsequent to a login denial
     */
    public void switchToLoginWindow(boolean isLoginChangeVoluntary) {
        this.chatWindow.setVisible(false);
        if(isLoginChangeVoluntary) {
            this.loginWindow.setLabelToLoginChange();
        } else {
            this.loginWindow.setLabelToLoginDenied();
        }
        this.loginWindow.setVisible(true);
    }

    /**
     * Open a login window. Called upon object creation.
     */
    private void start() {
        GUI thisBis = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                thisBis.loginWindow = new LoginWindow(thisBis);
            }
        });
    }

    /**
     * Notify a message reception.
     * @param receivedMessage the received message
     * @param sender the sender
     */
    public void notifyMessageReception(Message receivedMessage, User sender) {
        if(sender.equals(this.selectedUser)){
            this.chatWindow.getMessageHistoryPane().addMessageBubble(receivedMessage);
        } else {
            this.chatWindow.getUsersTabsPane().getUserTab(sender).addNotification();
        }
    }

    /**
     * Switch a user tab status to disconnected.
     * @param user the user you want to display as disconnected
     */
    public void disconnectUser(User user) {
        this.chatWindow.getUsersTabsPane().getUserTab(user).setToOffline();
        if(this.selectedUser.equals(user)) {
            this.chatWindow.getSendZonePane().disableSendZone();
        }
    }

    /**
     * Update a user login.
     * @param newLogin the new login
     * @param formerLogin the former login
     */
    public void updateUserLogin(String newLogin, String formerLogin) {
        this.chatWindow.getUsersTabsPane().getUserTab(formerLogin).setText(newLogin);
    }

    /**
     * Switch a user tab status to connected.
     * @param user the user you want to display as connected
     * @param formerLogin the former login
     */
    public void connectUser(User user, String formerLogin) {
        UserTab userTab = this.chatWindow.getUsersTabsPane().getUserTab(formerLogin);
        userTab.setToOnline();
        if(this.selectedUser.equals(user)) {
            this.chatWindow.getSendZonePane().enableSendZone();
        }
        userTab.setText(user.getLogin());
    }

    /**
     * Add a user to the users tabs panel.
     * @param user the user you want to add
     */
    public void addUser(User user) {
        boolean userPanelIsEmpty = this.chatWindow.getUsersTabsPane().isEmpty();

        this.chatWindow.getUsersTabsPane().addUser(user);
        //if the added user is the first one, it is automatically selected
        if(userPanelIsEmpty) {
            this.changeSelectedUserAndUpdateMessages(user);
        }
    }
}
