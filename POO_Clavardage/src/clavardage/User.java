package clavardage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class User {
    private String login;
    private String macAddress;
    private InetAddress ipAddress;
    private Socket socket; //null if no communication established with the user

    //used to create connected users
    public User(String login, InetAddress ipAddress, String macAddress) {
        this.login = login;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.socket = null;
    }

    //used to create disconnected users
    public User(String login, String macAddress) {
        this.login = login;
        this.ipAddress = null;
        this.macAddress = macAddress;
        this.socket = null;
    }

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

    public void connect(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getLogin() {
        return this.login;
    }

    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public boolean socketExists() {
        return (this.socket != null);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String toString() {
        return "" +
            "login : " + this.login + "\n" +
            "mac address : " + this.macAddress + "\n" +
            "ip address : " + this.ipAddress.getHostName();
    }

    public boolean isConnected() {
        return ipAddress != null;
    }

    public void changeLogin(String newLogin) {
        this.login = newLogin;
    }
}
