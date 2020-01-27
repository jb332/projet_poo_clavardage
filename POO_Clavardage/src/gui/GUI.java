package gui;

import clavardage.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI implements ActionListener {
    private static boolean instantiated = false;

    private Clavardage chat;

    private ChatWindow chatWindow;
    private LoginWindow loginWindow;

    private User selectedUser;

    private GUI(Clavardage chat) {
        this.chat = chat;
        this.start();
    }

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

    //changes the message displayed upon selecting another user
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

    private void launchChatWindow() {
        this.chatWindow = new ChatWindow(this);

        this.chatWindow.getUsersTabsPane().loadUsers(this.chat.getUsers().getList());
        //selecting a random user to start
        User selectedUser = this.chat.getUsers().getArbitraryUser();
        this.changeSelectedUserAndUpdateMessages(selectedUser);
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
                this.switchToLoginWindow(true);
                break;
            default:
                User selectedUser = this.chat.getUsers().getUserFromLogin(buttonName);
                changeSelectedUserAndUpdateMessages(selectedUser);
        }
    }

    public void switchToLoginWindow(boolean isLoginChangeVoluntary) {
        this.chatWindow.setVisible(false);
        if(isLoginChangeVoluntary) {
            this.loginWindow.setLabelToLoginChange();
        } else {
            this.loginWindow.setLabelToLoginDenied();
        }
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

    public void updateUserLogin(String newLogin, String formerLogin) {
        this.chatWindow.getUsersTabsPane().getUserTab(formerLogin).setText(newLogin);
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
            this.changeSelectedUserAndUpdateMessages(user);
        }
    }
}
