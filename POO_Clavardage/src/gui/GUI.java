package gui;

import clavardage.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI implements ActionListener {
    private Clavardage chat;

    private MessagesHistoryPanel messageHistoryPane;
    private UsersTabsPanel usersTabsPane;
    private SendZonePanel sendZonePane;
    private ChatWindow chatWindow;
    private LoginWindow loginWindow;

    private User selectedUser;

    public GUI(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

    //changes the message displayed upon selecting another user
    private void changeUserAndUpdateMessages(User selectedUser) {
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

    private void launchChatWindow() {
        this.chatWindow = new ChatWindow(this);

        this.chatWindow.getUsersTabsPane().loadUsers(this.chat.getUsers().getList());
        //selecting a random user to start
        User selectedUser = this.chat.getUsers().getArbitraryUser();
        this.changeUserAndUpdateMessages(selectedUser);
    }

    public void sendMessage(Message sentMessage, User receiver) {
        //send the message over the network and store it
        this.chat.sendAndStoreMessage(sentMessage, receiver);

        //display the message in history
        this.chatWindow.getMessageHistoryPane().addMessageBubble(sentMessage);

        //remove the text in the sendTextZone
        this.chatWindow.getSendZonePane().clearTextZone();
    }

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
                System.out.println("Change login button clicked");
                break;
            default:
                User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
                changeUserAndUpdateMessages(selectedUser);
        }

        /*
        if(buttonName.equals(">")) {
            String content = this.chatWindow.getSendZonePane().getText();
            if(!content.equals("")) {
                Message messageSent = new Message(content, Message.SENT);
                this.sendMessage(messageSent, this.selectedUser);
            }
        } else if(buttonName.equals("OK")) {
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
        } else {
            User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
            changeUserAndUpdateMessages(selectedUser);
        }
        */
    }

    public void switchToLoginWindow() {
        this.chatWindow.setVisible(false);
        this.loginWindow.setTitle("Le pseudo choisi est déjà pris, saisissez un autre pseudo : ");
        this.loginWindow.setVisible(true);
    }

    private void start() {
        GUI thisBis = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                thisBis.loginWindow = new LoginWindow(thisBis);
            }
        });
    }

    public void notifyMessageReception(Message receivedMessage, User sender) {
        if(sender.equals(this.selectedUser)){
            this.chatWindow.getMessageHistoryPane().addMessageBubble(receivedMessage);
        } else {
            this.chatWindow.getUsersTabsPane().getUserTab(sender).addNotification();
        }
    }

    public void disconnectUser(User user) {
        this.chatWindow.getUsersTabsPane().getUserTab(user).setToOffline();
        if(this.selectedUser.equals(user)) {
            this.chatWindow.getSendZonePane().disableSendZone();
        }
    }

    public void displayLoginChange(User user) {
        this.chatWindow.getUsersTabsPane().getUserTab(user).setText(user.getLogin());
    }

    //change color to connected and update login
    public void connectUser(User user, String formerLogin) {
        UserTab userTab = this.chatWindow.getUsersTabsPane().getUserTab(formerLogin);
        userTab.setToOnline();
        if(this.selectedUser.equals(user)) {
            this.chatWindow.getSendZonePane().enableSendZone();
        }
        userTab.setText(user.getLogin());
    }

    public void addUser(User user) {
        boolean userPanelIsEmpty = this.chatWindow.getUsersTabsPane().isEmpty();

        this.chatWindow.getUsersTabsPane().addUser(user);
        //if the added user is the first one, it is automatically selected
        if(userPanelIsEmpty) {
            this.changeUserAndUpdateMessages(user);
        }
    }
}
