package clavardage;

import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Clavardage {
    public NetworkManager net;
    public DataBaseInterface db;
    public CommunicationWindow mainWindow;

    private User me;
    //private ArrayList<User> connectedUsers;
    private History connectedUsersHistory;

    public Clavardage() {
        this.connectedUsersHistory = new History();
        this.net = new NetworkManager(this);
        this.db = new DataBaseInterface(this);
        this.mainWindow = new CommunicationWindow(this);
    }

    public boolean connectAndCheckLogin(String login) {
        boolean connected = this.net.connectAndCheckLogin(login);
        if(connected) {
            this.me = new User(login);
        }
        return connected;
    }

    //methode appelée par le NetworkManager
    public void storeReceivedMessage(Message message, User sender) {
        this.db.storeMessage(message, sender);
        this.mainWindow.notifyMessageReception(message, sender);
    }

    public boolean sendMessage(User receiver, String messageContent) {
        Message message = new Message(messageContent, MessageWay.SENT);
        this.net.sendMessage(message, receiver);
        this.mainWindow.notifyMessageSent(message, receiver);
        this.db.storeMessage(message, receiver);
        return false;
    }

    public ArrayList<User> getUsers() {
        return this.connectedUsersHistory.getUsers();
    }

    public User getUserFromLogin(String login) {
        return this.connectedUsersHistory.getUserFromLogin(login);
    }

    public ArrayList<Message> getMessages(User user) {
        return this.connectedUsersHistory.getMessages(user);
    }

    public User getUserFromIP(String ipAddress) {
        ArrayList<User> connectedUsers = this.connectedUsersHistory.getUsers();
        Iterator i = connectedUsers.iterator();
        User foundUser = null;
        while (i.hasNext() && foundUser == null) {
            User currentUser = (User)(i.next());
            if(currentUser.getIpAddress().equals(ipAddress)) {
                foundUser = currentUser;
            }
        }
        return foundUser;
    }

    public User getMe() {
        return this.me;
    }

    public static void main(String[] args) {
        Clavardage chat = new Clavardage();
//        if(args.length != 0) {
//            switch(args[0]) {
//                case "s":
//                case "send":
//                    try {
//                        chat.net.sendConnectionResquest("jb32", InetAddress.getByName("localhost")/*InetAddress.getByName("255.255.255.255")*/);
//                    } catch(Exception e) {
//                        System.out.println("connection request sending error");
//                    }
//                    break;
//                case "r":
//                case "receive":
//                    try {
//                        chat.net.receiveConnexionRequest("kikidu32");
//                    } catch(Exception e) {
//                        System.out.println("connection request receiving error");
//                    }
//                    break;
//                default:
//                    System.out.println("Bad argument usage");
//            }
//        }
    }
}
