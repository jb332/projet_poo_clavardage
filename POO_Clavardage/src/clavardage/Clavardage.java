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

    public void storeNewUser(User user) {
        this.db.storeUser(user);
    }

    public boolean sendMessage(String messageContent, String receiverLogin) {
        Message message = new Message(messageContent, MessageWay.SENT);
        User receiver = getUserFromLogin(receiverLogin);

        try {
            this.net.sendMessage(message, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public History getConnectedUsersHistory() {
        return this.connectedUsersHistory;
    }

    public void chooseLogin(String login) {
        this.me = new User(login, "", "");
    }

    public static void main(String[] args) throws Exception{
        Clavardage chat = new Clavardage();
        /* Test Connexion UDP
        if(args.length != 0) {
            switch(args[0]) {
                case "s":

                case "send":
                        if(chat.net.sendConnectionResquest("jb33", InetAddress.getByName("10.1.5.230"))) {

                        }

                    break;
                case "r":
                case "receive":
                	chat.chooseLogin("jb32");
                    break;
                default:
                    System.out.println("Bad argument usage");
            }
        }
*/

        /* Test envoi message */

        if(args.length != 0) {
        	switch(args[0]) {
            case "s":
            case "send":
                User dest = new User("jb32", "10.1.5.230", "");
            	chat.net.sendMessage(new Message("     test     et   un  ett deux  et trois    ", MessageWay.SENT), dest );
                chat.net.sendMessage(new Message("     Cest parti mon peeeeetiiiiit    ", MessageWay.SENT), dest );
            	break;
            case "r":
            case "receive":
            	chat.chooseLogin("jb32");
                break;
            default:
                System.out.println("Bad argument usage");

        	}

        }


    }

}
