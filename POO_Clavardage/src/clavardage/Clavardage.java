package clavardage;

import database.DataBaseInterface;
import gui.GUI;
import network.NetworkManager;

import java.net.InetAddress;
import java.util.ArrayList;

public class Clavardage {
    public NetworkManager net;
    public DataBaseInterface db;
    public GUI gui;

    private Users users;
    private String myLogin;

    private Clavardage() {
        this.db = DataBaseInterface.instantiate(this);

        this.users = new Users();
        this.users.addUsers(this.db.getUsers());

        this.gui = GUI.instantiate(this);

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

    public boolean treatConnectionRequest(String requestingUserLogin, InetAddress requestingUserIPAddress, String requestingUserMacAddress) {
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
        boolean isAChangeLoginRequest = false;
        //if the new connected user already exists as a disconnected user, it is only modified instead of being created
        User alreadyExistingUser = this.users.getUserFromMacAddress(requestingUserMacAddress);
        //there is no user, connected or disconnected for that mac address, so a new connected user is created
        if(alreadyExistingUser == null) {
            User newConnectedUser = new User(requestingUserLogin, requestingUserIPAddress, requestingUserMacAddress);
            this.users.addUser(newConnectedUser);
            this.gui.addUser(newConnectedUser);
            this.db.addUser(newConnectedUser);
        } else {
            String formerLogin = alreadyExistingUser.getLogin();
            alreadyExistingUser.changeLogin(requestingUserLogin);
            //there is already a connected user for that mac address, this means the user just wants to change his pseudo
            if(alreadyExistingUser.isConnected()) {
                this.gui.updateUserLogin(requestingUserLogin, formerLogin);
                isAChangeLoginRequest = true;
            //there is already a disconnected user for that mac address, so his status is changed to connected
            } else {
                alreadyExistingUser.connect(requestingUserIPAddress);
                this.gui.connectUser(alreadyExistingUser, formerLogin);
            }
            this.db.updateLogin(alreadyExistingUser);
        }
        return isAChangeLoginRequest;
    }

    public void disconnectUser(String macAddress) {
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

    public boolean isLoginAvailable(String login, String macAddress) {
        return !this.myLogin.equals(login) && this.users.isLoginAvailableAmongOtherUsers(login, macAddress);
    }

    public void notifyLoginDeny() {
        this.gui.switchToLoginWindow(false);
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
