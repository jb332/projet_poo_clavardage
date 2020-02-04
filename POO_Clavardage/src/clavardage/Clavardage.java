package clavardage;

import database.DataBaseInterface;
import gui.GUI;
import network.NetworkManager;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Main class and controller.
 */
public class Clavardage {
    /**
     * Used to ensure single instantiation.
     */
    private static boolean instantiated = false;

    /**
     * Singleton managing the network.
     */
    public NetworkManager net;
    /**
     * Singleton for interactions with the database.
     */
    public DataBaseInterface db;
    /**
     * Singleton for the graphical interface.
     */
    public GUI gui;

    /**
     * Known users.
     */
    private Users users;
    /**
     * My login.
     */
    private String myLogin;

    /**
     * Constructor.
     */
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

    /**
     * Create an instance of the class. It raises an error if called twice.
     */
    public static synchronized void instantiate() {
        if(!Clavardage.instantiated) {
            Clavardage.instantiated = true;
            new Clavardage();
        } else {
            System.out.println("Fatal error : DataBaseInterface can not be instantiated twice");
            System.exit(1);
        }
    }

    /**
     * Send a connection or login change request (create and launch the network manager if this is a connection request).
     * @param login the login pickeds
     */
    public void connect(String login) {
        if(this.net == null) {
            this.net = NetworkManager.instantiate(this);
        }
        this.net.sendConnectionRequest(login);
    }

    /**
     * Treat a received message (store it in the database and notify the graphical interface).
     * @param message received message
     * @param sender sender
     */
    public void treatReceivedMessage(Message message, User sender) {
        this.db.storeMessage(message, sender);
        this.gui.notifyMessageReception(message, sender);
    }

    /**
     * Treat a received connection request or response (or a change login request). It consequently adds a user, or simply connect it and changes his pseudo depending on his current status.
     * @param requestingUserLogin the login of the requesting or responding user
     * @param requestingUserIPAddress the IP address of the requesting or responding user
     * @param requestingUserMacAddress the MAC address of the requesting or responding user
     * @return true if the requesting user wants a login change, false if the requesting user wants a connection. It is determined by checking if the requesting user exists and is already connect.
     */
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

    /**
     * Disconnect the user having the given MAC address.
     * @param macAddress the MAC address of the user you want to disconnect
     */
    public void disconnectUser(String macAddress) {
        User userToRemove = this.users.getUserFromMacAddress(macAddress);
        if(userToRemove != null) {
            userToRemove.disconnect();
            this.gui.disconnectUser(userToRemove);
        }
    }

    /**
     * Send and store a message.
     * @param message the message you want to send
     * @param receiver the user you want to receive your message
     */
    public void sendAndStoreMessage(Message message, User receiver) {
        try {
            this.net.sendMessage(message, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.db.storeMessage(message, receiver);
    }

    /**
     * Tell if the login picked by a remote user is available.
     * @param login the login you want to check the availability of
     * @param macAddress the macAddress of the requesting user
     * @return true if the picked login is available, false otherwise
     */
    public boolean isLoginAvailable(String login, String macAddress) {
        return !this.myLogin.equals(login) && this.users.isLoginAvailableAmongOtherUsers(login, macAddress);
    }

    /**
     * Notify the graphical interface to switch to login window because the connection was denied (login already used).
     */
    public void notifyLoginDeny() {
        this.gui.switchToLoginWindow(false);
    }

    /**
     * Get the known users.
     * @return known users
     */
    public Users getUsers() {
        return this.users;
    }

    /**
     * Get the messages exchanged with a user
     * @param user the user you want to get the exchanged messages of
     * @return a list of messages
     */
    public ArrayList<Message> getMessages(User user) {
        return this.db.getMessages(user);
    }

    /**
     * Set my login.
     * @param myLogin the login you want to have
     */
    public void setMyLogin(String myLogin) {
        this.myLogin = myLogin;
    }

    /**
     * Get my login.
     * @return my login
     */
    public String getMyLogin() {
        return this.myLogin;
    }

    public static void main(String[] args) {
        Clavardage.instantiate();
    }
}
