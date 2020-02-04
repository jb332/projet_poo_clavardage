package network;

import clavardage.Clavardage;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A thread to listen for connection requests.
 */
public class ConnectionRequestListening extends Thread {
    /**
     * The controller.
     */
    private Clavardage chat;
    /**
     * The UDP socket used to receive and send login messages.
     */
    private DatagramSocket udpSocket;


    /**
     * Constructor.
     * @param chat the controller
     * @param udpSocket the UDP socket used for communication
     */
    protected ConnectionRequestListening(Clavardage chat, DatagramSocket udpSocket){
        this.chat = chat;
        this.udpSocket = udpSocket;
        super.start();
    }

    /**
     * The method launched by the thread. It listens for incoming UDP messages and reacts in the appropriate way.
     */
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

                        if (this.chat.isLoginAvailable(requestingUserLogin, requestingUserMacAddress)) {
                            boolean isAChangeLoginRequest = this.chat.treatConnectionRequest(requestingUserLogin, requestingUserIpAddress, requestingUserMacAddress);
                            if(!isAChangeLoginRequest) {
                                (new ResponseLoginPacket(ResponseLoginPacket.GRANTED, this.chat.getMyLogin(), NetworkManager.getMyMacAddress())).sendPacket(this.udpSocket, requestingUserIpAddress);
                            }
                        } else {
                            (new ResponseLoginPacket(ResponseLoginPacket.DENIED)).sendPacket(this.udpSocket, requestingUserIpAddress);
                        }
                        break;
                    case "RES":
                        ResponseLoginPacket receivedResponseLoginPacket = (ResponseLoginPacket) receivedLoginPacket;

                        if(receivedResponseLoginPacket.isLoginGranted()) {
                            String respondingUserLogin = receivedResponseLoginPacket.getRespondingUserLogin();
                            String respondingUserMacAddress = receivedResponseLoginPacket.getRespondingUserMacAddress();
                            InetAddress respondingUserIpAddress = receivedResponseLoginPacket.getSourceAddress();

                            this.chat.treatConnectionRequest(respondingUserLogin, respondingUserIpAddress, respondingUserMacAddress);
                        } else {
                            this.chat.notifyLoginDeny();
                        }
                        break;
                    case "END":
                        LogoutPacket receivedLogoutPacket = (LogoutPacket) receivedLoginPacket;

                        String loggingOutUserMacAddress = receivedLogoutPacket.getRequestingUserMacAddress();
                        this.chat.disconnectUser(loggingOutUserMacAddress);
                }
            }
        }
    }
}
