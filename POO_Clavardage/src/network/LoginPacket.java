package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class LoginPacket {
    private InetAddress sourceAddress = null;

    protected InetAddress getSourceAddress() {
        return this.sourceAddress;
    }

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

    protected abstract String getDataToSend();

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
