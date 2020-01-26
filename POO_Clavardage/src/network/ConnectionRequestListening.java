package network;

import clavardage.Clavardage;
import clavardage.User;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionRequestListening extends Thread {
    private Clavardage chat;
    private DatagramSocket udpSocket;


    protected ConnectionRequestListening(Clavardage chat, DatagramSocket udpSocket){
        this.chat = chat;
        this.udpSocket = udpSocket;
        super.start();
    }

    public void run() {
        while(true) {
            LoginPacket receivedLoginPacket = LoginPacket.receivePacket(this.udpSocket);
            //test if packet is valid and not emitted by this machine
            if(receivedLoginPacket != null && !receivedLoginPacket.getSourceAddress().equals(NetworkManager.getMyIpAddress())) {
                switch (receivedLoginPacket.getFlag()) {
                    case "REQ":
                        RequestLoginPacket receivedRequestLoginPacket = (RequestLoginPacket) receivedLoginPacket;
                        String requestingUserLogin = receivedRequestLoginPacket.getRequestingUserLogin();
                        String requestingUserMacAddress = receivedRequestLoginPacket.getRequestingUserMacAddress();
                        InetAddress requestingUserIpAddress = receivedRequestLoginPacket.getSourceAddress();

                        if (this.chat.isLoginAvailable(requestingUserLogin)) {
                            (new ResponseLoginPacket(true, this.chat.getMyLogin(), NetworkManager.getMyMacAddress())).sendPacket(this.udpSocket, requestingUserIpAddress);
                            System.out.println(requestingUserIpAddress.getHostAddress());
                            User newConnectedUser = new User(requestingUserLogin, requestingUserIpAddress, requestingUserMacAddress);
                            this.chat.addConnectedUser(newConnectedUser);
                        } else {
                            (new ResponseLoginPacket(false)).sendPacket(this.udpSocket, requestingUserIpAddress);
                        }
                        break;
                    case "RES":
                        ResponseLoginPacket receivedResponseLoginPacket = (ResponseLoginPacket) receivedLoginPacket;

                        System.out.println(receivedResponseLoginPacket.isLoginGranted());
                        System.out.println(receivedResponseLoginPacket.getDataToSend());

                        if(receivedResponseLoginPacket.isLoginGranted()) {
                            String respondingUserLogin = receivedResponseLoginPacket.getRespondingUserLogin();
                            String respondingUserMacAddress = receivedResponseLoginPacket.getRespondingUserMacAddress();
                            InetAddress respondingUserIpAddress = receivedResponseLoginPacket.getSourceAddress();

                            User newConnectedUser = new User(respondingUserLogin, respondingUserIpAddress, respondingUserMacAddress);
                            System.out.println(newConnectedUser);
                            this.chat.addConnectedUser(newConnectedUser);
                        } else {
                            this.chat.notifyLoginDeny();
                        }
                        break;
                    case "END":
                        LogoutPacket receivedLogoutPacket = (LogoutPacket) receivedLoginPacket;

                        String loggingOutUserMacAddress = receivedLogoutPacket.getRequestingUserMacAddress();
                        this.chat.removeUser(loggingOutUserMacAddress);
                }
            }
        }
    }
}
