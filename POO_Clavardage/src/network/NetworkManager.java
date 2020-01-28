package network;

import clavardage.Clavardage;
import clavardage.Message;
import clavardage.User;

import java.net.*;
import java.io.*;
import java.util.Enumeration;

public class NetworkManager {

    private Clavardage chat;
    private DatagramSocket udpSocket;

    protected static final int loginPort = 10000;
    protected static final int messagePort = 20000;

    public static final InetAddress broadcastIPAddress = NetworkManager.stringToInetAddress("255.255.255.255");

    private static InetAddress stringToInetAddress(String address) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("Fatal error : could not get address from IP : \"" + address + "\"");
            System.exit(1);
        }
        return inetAddress;
    }

    private static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static boolean hasIPv4Address(NetworkInterface networkInterface) {
        Enumeration<InetAddress> interfaceAddresses = networkInterface.getInetAddresses();
        boolean iPv4AddressFound = false;
        while(!iPv4AddressFound && interfaceAddresses.hasMoreElements()) {
            InetAddress currentAddress = interfaceAddresses.nextElement();
            System.out.println(currentAddress.getHostName());
            if(currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
                iPv4AddressFound = true;
            }
        }
        return iPv4AddressFound;
    }

    private static NetworkInterface getActiveInterface() {
        NetworkInterface activeInterface = null;
        try {
            Enumeration<NetworkInterface> interfacesList = NetworkInterface.getNetworkInterfaces();
            while (activeInterface == null && interfacesList.hasMoreElements()) {
                NetworkInterface currentInterface = interfacesList.nextElement();
                if (currentInterface.isUp() && !currentInterface.isLoopback() && hasIPv4Address(currentInterface)) {
                    activeInterface = currentInterface;
                }
            }
        } catch(SocketException e) {
            System.out.println("Fatal error : impossible to get the network interfaces list or get an interface state" + e);
            System.exit(1);
        }
        if(activeInterface == null) {
            System.out.println("Fatal error : no active interface found on this machine");
            System.exit(1);
        }
        return activeInterface;
    }

    protected static String getMyMacAddress() {
        String myMacAddress = null;
        try {
            byte[] byteMacAddress = NetworkManager.getActiveInterface().getHardwareAddress();
            myMacAddress = NetworkManager.bytesToHex(byteMacAddress);
        } catch (SocketException e) {
            System.out.println("Fatal error : could not get mac address from active interface" + e);
            System.exit(1);
        }
        return myMacAddress;
    }

    protected static InetAddress getMyIpAddress() {
        Enumeration<InetAddress> interfaceAddresses = NetworkManager.getActiveInterface().getInetAddresses();
        InetAddress myIpAddress = null;
        while(myIpAddress == null && interfaceAddresses.hasMoreElements()) {
            InetAddress currentAddress = interfaceAddresses.nextElement();
            System.out.println(currentAddress.getHostName());
            if(currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
                myIpAddress = currentAddress;
            }
        }
        if(myIpAddress == null) {

            System.out.println("Fatal error : no valid IP address found for the active interface");
            System.exit(1);
        }
        return myIpAddress;
    }

    public NetworkManager(Clavardage chat) {
        this.chat = chat;

        //create UDP socket
        try {
            this.udpSocket = new DatagramSocket(NetworkManager.loginPort);
        } catch(SocketException e) {
            System.out.println("UDP Socket creation error : " + e);
        }

        //create and launch a thread to answer connection requests
        new ConnectionRequestListening(this.chat, this.udpSocket);
        //create and launch a thread to store incoming messages
        new MessageReceiving(chat);

        NetworkManager thisBis = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                thisBis.sendDisconnectionWarning();
            }
        });
    }

    public void sendConnectionRequest(String login) {
        String macAddress = NetworkManager.getMyMacAddress();
        RequestLoginPacket requestLoginPacket = new RequestLoginPacket(login, macAddress);
        requestLoginPacket.sendPacket(this.udpSocket, NetworkManager.broadcastIPAddress);
        System.out.println("Login request message sent");
    }

    public void sendDisconnectionWarning() {
        String macAddress = NetworkManager.getMyMacAddress();
        LogoutPacket logoutPacket = new LogoutPacket(macAddress);
        logoutPacket.sendPacket(this.udpSocket, NetworkManager.broadcastIPAddress);
        System.out.println("Logout message sent");
    }

    public void sendMessage(Message message, User receiver) {
        Socket socket = null;
        //check if a connection already exists
        if(receiver.socketExists()) {
            //a connection already exists with this user so we just use the saved socket
            socket = receiver.getSocket();
            //System.out.println("A socket already exists : " + socket);
        } else {
            //it is the first time a message is exchanged with this user so we create a socket and we save it
            InetAddress destAddr = receiver.getIpAddress();
            try {
                socket = new Socket(destAddr, NetworkManager.messagePort);
            } catch (IOException e) {
                System.out.println("Could not create a socket to exchange messages with + " + receiver + " : " + e);
                System.exit(1);
            }
            receiver.setSocket(socket);
            //System.out.println("Socket created : " + socket);

            //create a thread to listen on this socket because the receiver will respond with this socket
            new MessageReceivingThread(socket, this.chat);
        }

        try {
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            //oos.writeChars(message.getContent());
            oos.writeUTF(message.getContent());
        } catch (IOException e) {
            System.out.println("Could not send message to " + receiver + " : " + e);
            System.exit(1);
        }
        //oos.close();
    }
}