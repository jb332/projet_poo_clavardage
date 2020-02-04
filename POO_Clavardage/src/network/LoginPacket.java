package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * An abstract class representing all the packets sent to handle login changes, connections and disconnections.
 */
public abstract class LoginPacket {
    /**
     * The source address of the packet.
     */
    private InetAddress sourceAddress = null;

    /**
     * Get the source address of this packet.
     * @return the source address of this packet
     */
    protected InetAddress getSourceAddress() {
        return this.sourceAddress;
    }

    /**
     * Get the type of this packet as a three letters flag.
     * @return the flag of this packet
     */
    protected String getFlag() {
        String className = this.getClass().getSimpleName();
        String flag = null;
        switch (className) {
            case "RequestLoginPacket":
                flag = "REQ";
                break;
            case "ResponseLoginPacket":
                flag = "RES";
                break;
            case "LogoutPacket":
                flag = "END";
                break;
            default:
                System.out.println("Wrong class name for LoginPacket : \"" + className + "\"");
                System.exit(1);
        }
        return flag;
    }

    /**
     * Deserialization method used to rebuild a login packet from data received on a socket. The login packet created must be cast to the right type. To know this type, the "getFlag" method must be used.
     * @param udpSocket the UDP socket you want to listen on for incoming packets.
     * @return a login packet.
     */
    protected static LoginPacket receivePacket(DatagramSocket udpSocket) {
        byte buffer[] = new byte[10000];
        DatagramPacket dataReceived = new DatagramPacket(buffer, buffer.length);
        try {
            udpSocket.receive(dataReceived);
        } catch (IOException e) {
            System.out.println("Could not receive data using UDP socket : " + e);
            System.exit(1);
        }
        String rawData = new String(dataReceived.getData());
        String[] data = rawData.split(";");
        String flag = data[0];
        LoginPacket loginPacket = null;
        switch(flag) {
            case "REQ":
                loginPacket = new RequestLoginPacket(data[1], data[2]);
                break;
            case "RES":
                if(data[1].equals("no")) {
                    loginPacket = new ResponseLoginPacket(ResponseLoginPacket.DENIED);
                } else {
                    loginPacket = new ResponseLoginPacket(ResponseLoginPacket.GRANTED, data[1], data[2]);
                }
                break;
            case "END":
                loginPacket = new LogoutPacket(data[1]);
                break;
            default:
                System.out.println("Error : UDP packet with unknown flag received.");
        }
        if(loginPacket != null) {
            loginPacket.sourceAddress = dataReceived.getAddress();
        }
        return loginPacket;
    }

    /**
     * Serialization abstract method used to convert this packet to a string that is to be sent over the network.
     * @return the serialized packet
     */
    protected abstract String getDataToSend();

    /**
     * Send the packet over the network to an IP address using an UDP socket.
     * @param udpSocket the UDP socket you want to use to send the packet over the network
     * @param destinationAddress the destination IP address
     */
    protected void sendPacket(DatagramSocket udpSocket, InetAddress destinationAddress) {
        byte buffer[] = this.getDataToSend().getBytes();
        try {
            DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, destinationAddress, NetworkManager.loginPort);
            udpSocket.send(dataSend);
        } catch (IOException e) {
            System.out.println("Could not send data using UDP socket : " + e);
            System.exit(1);
        }
    }
}
