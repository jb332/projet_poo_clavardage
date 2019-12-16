package clavardage;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class User {    private String login;
    private String macAddress;
    private String ipAddress;

    private String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    private String[] getMacIP() throws Exception {

        Enumeration<NetworkInterface> lst_int =  NetworkInterface.getNetworkInterfaces();
        NetworkInterface adrActive = null;
        boolean trouve = false;
        while(!trouve & lst_int.hasMoreElements()) {
            adrActive = lst_int.nextElement();
            trouve = (adrActive.isUp() && !adrActive.isLoopback());
        }

        byte[] mac= adrActive.getHardwareAddress();
        adrActive.getInetAddresses().nextElement();
        ArrayList<InterfaceAddress> ip = (ArrayList<InterfaceAddress>) adrActive.getInterfaceAddresses();

        String infos[] = {this.bytesToHex(mac), (ip.get(1).getAddress().getHostAddress())};
        return infos;

    }

    public User(String login) {
        this.login = login;
        try {
            String infos[] = this.getMacIP();

            this.macAddress = infos[0];
            this.ipAddress = infos[1];
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
