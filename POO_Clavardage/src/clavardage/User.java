package clavardage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A class representing a user
 */
public class User {
    /**
     * This user's login.
     */
    private String login;
    /**
     * This user's MAC address. It is used for identification.
     */
    private String macAddress;
    /**
     * This user's IP address. It is used to communicate with other users, it is null when this user is disconnected.
     */
    private InetAddress ipAddress;
    /**
     * The socket used to communicate with the person associated with this user
     */
    private Socket socket;

    /**
     * Constructor for creating connected users. Note : no socket is bound when a user is created.
     * @param login this user's login
     * @param ipAddress this user's IP address
     * @param macAddress this user's MAC address
     */
    public User(String login, InetAddress ipAddress, String macAddress) {
        this.login = login;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.socket = null;
    }

    /**
     * Constructor for creating a disconnected user. Note : no socket is bound when a user is created.
     * @param login this user's login
     * @param macAddress this user's MAC address
     */
    public User(String login, String macAddress) {
        this.login = login;
        this.ipAddress = null;
        this.macAddress = macAddress;
        this.socket = null;
    }

    /**
     * Change this user's status to 'disconnected"
     */
    public void disconnect() {
        this.ipAddress = null;
        if(this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.out.println("Could not close socket for user :\n" + this);
            } finally {
                this.socket = null;
            }
        }
    }

    /**
     * Change this user's status to "connected" and set an IP address
     * @param ipAddress the IP address you want to associate with this user
     */
    public void connect(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Bind a socket to this user.
     * @param socket the socket you want to associate with this user
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Get this user's login.
     * @return this user's login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Get this user's IP address.
     * @return this user's IP address
     */
    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    /**
     * Get this user's MAC address.
     * @return this user's MAC address
     */
    public String getMacAddress() {
        return this.macAddress;
    }

    /**
     * Tell if a socket is associated with this user.
     * @return true if a socket is associated with this user, false otherwise
     */
    public boolean socketExists() {
        return (this.socket != null);
    }

    /**
     * Get the socket associated with this user.
     * @return the socket associated with this user, or null if no communication has been established yet
     */
    public Socket getSocket() {
        return this.socket;
    }

    public String toString() {
        return "" +
            "login : " + this.login + "\n" +
            "mac address : " + this.macAddress + "\n" +
            "ip address : " + this.ipAddress.getHostName();
    }

    /**
     * Tell if this user is connected.
     * @return true if the user is connected, false otherwise
     */
    public boolean isConnected() {
        return ipAddress != null;
    }

    /**
     * Change this user's login
     * @param newLogin the login you want to set
     */
    public void changeLogin(String newLogin) {
        this.login = newLogin;
    }
}
