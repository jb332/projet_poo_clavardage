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
        this.net = new NetworkManager(this);
        this.db = new DataBaseInterface(this);
        this.mainWindow = new CommunicationWindow(this);
        this.connectedUsersHistory = new History();
    }

    public boolean connectAndCheckLogin(String login) {
        boolean connected = this.net.connectAndCheckLogin(login);
        if(connected) {
            this.me = new User(login);
        }
        return connected;
    }

    //methode appelée par le NetworkManager
    public void storeReceivedMessage(Message message) {
        this.db.storeMessage(message);
        this.mainWindow.notifyMessageReception(message);
    }

    public boolean sendMessage(User recipient, String messageContent) {
        Message message = new Message(messageContent, this.me, recipient);
        this.net.sendMessage(message);
        this.mainWindow.notifyMessageSent(message);
        this.db.storeMessage(message);
        return false;
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
    
    public History getConnectedUsersHistory() {
    	return this.connectedUsersHistory;
    }

    public static void main(String[] args) {
        Clavardage chat = new Clavardage();

        if(args.length != 0) {
            switch(args[0]) {
                case "s":
                case "send":
                    try {
                        chat.net.sendConnectionResquest("jb32", InetAddress.getByName("localhost")/*InetAddress.getByName("255.255.255.255")*/);
                    } catch(Exception e) {
                        System.out.println("connection request sending error");
                    }
                    break;
                case "r":
                case "receive":
                    try {
                        chat.net.receiveConnexionRequest("kikidu32");
                    } catch(Exception e) {
                        System.out.println("connection request receiving error");
                    }
                    break;
                default:
                    System.out.println("Bad argument usage");
            }
        }
    }
}
