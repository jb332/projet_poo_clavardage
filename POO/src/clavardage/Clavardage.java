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
    
    public void storeNewUser(User user) {
    	this.db.storeUser(user);
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
    
    public void chooseLogin(String login) {
    		this.me = new User(login, "", "");
    }

<<<<<<< HEAD
    public static void main(String[] args) throws Exception {
=======
    public static void main(String[] args) throws Exception{
>>>>>>> dd1efad9619b9f018a2e38c4cdc17e03427eed59
        Clavardage chat = new Clavardage();
        /* Test Connexion UDP
        if(args.length != 0) {
            switch(args[0]) {
                case "s":
<<<<<<< HEAD
                case "send":
                    try {
                        chat.net.sendConnectionResquest("jb33", InetAddress.getByName("10.1.5.91"));
                    } catch(Exception e) {
                        System.out.println(e);
                    }
=======
                case "send":                    
                        if(chat.net.sendConnectionResquest("jb33", InetAddress.getByName("10.1.5.91"))) {
                        	chat.net.sendMessage("je t'envoie un message", InetAddress.getByName("10.1.5.91"));
                        }                  	               
>>>>>>> dd1efad9619b9f018a2e38c4cdc17e03427eed59
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
        
        if(args.length != 0) {
        	switch(args[0]) {
            case "s":
            case "send":
            	chat.net.sendMessage("hellooo", InetAddress.getByName("10.1.5.91"));
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
