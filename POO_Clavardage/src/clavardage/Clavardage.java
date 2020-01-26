package clavardage;

import database.DataBaseInterface;
import gui.GUI;
import network.NetworkManager;

import java.util.ArrayList;

public class Clavardage {
    public NetworkManager net;
    public DataBaseInterface db;
    public GUI gui;

    private Users users;
    private String myLogin;

    public Clavardage() {
        this.db = new DataBaseInterface(this);

        this.users = new Users();
        this.users.addUsers(this.db.getUsers());

        this.gui = new GUI(this);

        //plan database shutdown when the user leaves the application
        Clavardage thisBis = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                thisBis.users.shutdownSockets();
            }
        });
    }

    public void connect(String login) {
        if(this.net == null) {
            this.net = new NetworkManager(this);
        }
        this.net.sendConnectionRequest(login);
    }

    //method called by the NetworkManager
    public void treatReceivedMessage(Message message, User sender) {
        this.db.storeMessage(message, sender);
        this.gui.notifyMessageReception(message, sender);
    }

    public void addConnectedUser(User connectedUser) {
        //penser a exclure l'utilisateur deconnecté correspondant en adresse mac avec l'utilisateur connecté ajouté
        /*
        User disconnectedUserHavingTheSameLogin = this.users.getUserFromLogin(connectedUser.getLogin());
        if(disconnectedUserHavingTheSameLogin != null) {
            //to prevent a situation where a connected user has the same login than a disconnected one, the disconnected user mac address is appened to its login
            disconnectedUserHavingTheSameLogin.changeLogin(disconnectedUserHavingTheSameLogin.getLogin() + " (" + disconnectedUserHavingTheSameLogin.getMacAddress() + ")");
            //login changed is actualized on the GUI
            this.gui.displayLoginChange(disconnectedUserHavingTheSameLogin);
        }
        */

        //if the new connected user already exists as a disconnected user, it is only modified instead of being created
        User alreadyExistingDisconnectedUser = this.users.getUserFromMacAddress(connectedUser.getMacAddress());
        if(alreadyExistingDisconnectedUser == null) {
            this.users.addUser(connectedUser);
            this.gui.addUser(connectedUser);
            this.db.addUser(connectedUser);
        } else {
            System.out.println("User already exists");
            String formerLogin = alreadyExistingDisconnectedUser.getLogin();
            alreadyExistingDisconnectedUser.connect(connectedUser.getIpAddress());
            alreadyExistingDisconnectedUser.changeLogin(connectedUser.getLogin());
            this.gui.connectUser(alreadyExistingDisconnectedUser, formerLogin);
            this.db.updateLogin(alreadyExistingDisconnectedUser);
        }
    }

    public void removeUser(String macAddress) {
        User userToRemove = this.users.getUserFromMacAddress(macAddress);
        if(userToRemove != null) {
            userToRemove.disconnect();
            this.gui.disconnectUser(userToRemove);
        }
    }

    public boolean sendAndStoreMessage(Message message, User receiver) {
        try {
            this.net.sendMessage(message, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.db.storeMessage(message, receiver);
        return false;
    }

    public boolean isLoginAvailable(String login) {
        return !this.myLogin.equals(login) && this.users.isLoginAvailableAmongOtherUsers(login);
    }

    public void notifyLoginDeny() {
        this.gui.switchToLoginWindow();
    }

    public Users getUsers() {
        return this.users;
    }

    public ArrayList<Message> getMessages(User user) {
        return this.db.getMessages(user);
    }

    public void setMyLogin(String myLogin) {
        this.myLogin = myLogin;
    }

    public String getMyLogin() {
        return this.myLogin;
    }

    public static void main(String[] args) {
        new Clavardage();
    }
}
