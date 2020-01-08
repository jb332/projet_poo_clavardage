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
    private Users connectedUsers;

    public Clavardage(Integer userNumber, String distantIP) {
        //connection phase
        //String login = "Jake";
        //with connection answers from the other agents, we build a user list
        //if the login is accepted, we create this user with it
        //this.me = new User(login);

        this.connectedUsers = new Users();

        switch(userNumber) {
            case 1:
                this.me = new User("J-B");
                this.connectedUsers.addUser(new User("Rémy", distantIP, "64:00:6a:59:60:d7"));
                break;
            case 2:
                this.me = new User("Rémy");
                this.connectedUsers.addUser(new User("J-B", distantIP, "f8:28:19:73:f2:1f"));
                break;
            default:
                this.me = new User("Moi");
                break;
        }

        this.net = new NetworkManager(this);
        this.db = new DataBaseInterface(this);
        this.mainWindow = new CommunicationWindow(this);

        //plan database shutdown when the user leaves the application
        Clavardage thisBis = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                thisBis.connectedUsers.shutdownSockets();
            }
        });
    }

    public boolean connectAndCheckLogin(String login) {
        boolean connected = this.net.connectAndCheckLogin(login);
        if(connected) {
            this.me = new User(login);
        }
        return connected;
    }

    //methode appelée par le NetworkManager
    public void treatReceivedMessage(Message message, User sender) {
        this.db.storeMessage(message, sender);
        this.mainWindow.notifyMessageReception(message, sender);
    }

    public void addUser(User user) {
        this.connectedUsers.addUser(user);
        this.mainWindow.addUser(user);
    }

    public boolean sendMessage(Message message, User receiver) {
        try {
            this.net.sendMessage(message, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.db.storeMessage(message, receiver);
        return false;
    }

    public Users getUsers() {
        return this.connectedUsers;
    }

    public ArrayList<Message> getMessages(User user) {
        return this.db.getMessages(user);
    }

    public User getMe() {
        return this.me;
    }

    public void chooseLogin(String login) {
        this.me = new User(login, "", "");
    }

    public static void main(String[] args) {
        Integer userNumber;
        if(args.length != 0) {
            userNumber = Integer.parseInt(args[0]);
        } else {
            userNumber = 0;
        }

        String distantIP;
        if(args.length > 1) {
            distantIP = args[1];
        } else {
            distantIP = "no_ip";
        }

        new Clavardage(userNumber, distantIP);
        /* Test Connexion UDP
        if(args.length != 0) {
            switch(args[0]) {
                case "s":

                case "send":
                        if(chat.net.sendConnectionResquest("jb33", InetAddress.getByName("10.1.5.91"))) {
                        	chat.net.sendMessage("je t'envoie un message", InetAddress.getByName("10.1.5.91"));
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

        /* Test envoi message

        if(args.length != 0) {
        	switch(args[0]) {
            case "s":
            case "send":
            	chat.net.sendMessage("hellooo", InetAddress.getByName("10.32.1.233"));
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
        /*
        User test = new User("oui");
        System.out.println(test.getIpAddress() + "   " +test.getMacAddress());
        */
    }
}
