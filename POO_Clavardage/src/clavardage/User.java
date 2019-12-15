package clavardage;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class User {
    private String login;
    private String macAddress;
    private String ipAddress;

    public User(String login) {
        this.login = login;
        try {
            this.macAddress = NetworkInterface.getByIndex(0).getHardwareAddress().toString();
            this.ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e) {
            System.out.println("error while trying to get ip or mac address");
        }
    }

    public User(String login, String ipAddress, String macAddress) {
        this.login = login;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    public String getLogin() {
        return this.login;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String toString() {
        return "login : " + this.login + "\nmac address : " + this.macAddress + "\nip address : " + this.ipAddress;
    }
}
