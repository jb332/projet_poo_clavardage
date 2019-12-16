package clavardage;

import java.net.*;
import java.io.*;


public class NetworkManager {

    private Clavardage chat;

    public static final int port_connexion = 10000;
    public static final int port_reponse_connexion = 10001;
    public static final int port_message = 20000;

    public NetworkManager(Clavardage chat) {
        this.chat = chat;
        //create and launch a thread to answer connection requests
        new ConnectionRequestListeningThread(chat);
        //create and launch a thread to store incoming messages
        new MessageReceiving(chat);
    }

    public void sendMessage(Message message) {
    }

    public boolean connectAndCheckLogin(String login) {
        return false;
    }





    public boolean sendConnectionResquest(String login, InetAddress addbroadcast) throws Exception {
        System.out.println("in sendConnectionRequest method");
        //emission de la demande
        String envoi = login+",tesst";
        byte buffer[] = envoi.getBytes();
        DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, addbroadcast, port_connexion);
        DatagramSocket socket = new DatagramSocket();
        socket.send(dataSend);

        //réception réponse
        byte bufferReception[] = new byte[10];
        DatagramSocket socketreception = new DatagramSocket(port_reponse_connexion);
        DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
        socketreception.receive(dataReceived);
        socket.close();
        socketreception.close();
        String answer = new String(dataReceived.getData());
        System.out.println(answer);
        if(!answer.equals("no")) {
            System.out.println("Connexion établie");
            return true;
        }
        else {
            System.out.println("Nous n'avons pas pu vous connecter");
            return false;
        }

    }

    public void sendMessage(Message message, User receiver) throws Exception{
        InetAddress destAddr = InetAddress.getByName(receiver.getIpAddress());
        Socket socket = new Socket(destAddr, port_message);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message.getContent());
        oos.close();
        socket.close();
    }

    /*
    public void receiveConnexionRequest(String mylogin) throws Exception {
        System.out.println("in receiveConnectionRequest method");

        byte bufferReception[] = new byte[10];
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
        socket.receive(dataReceived);
        String login = dataReceived.getData().toString();
        String answer = "";
        if (login.equals(mylogin))
            answer = "no";
        else
            answer = "ok";

        byte buffer[] = answer.getBytes();
        DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, dataReceived.getAddress(), port_connexion);

        socket.send(dataSend);
        //réception réponse

        socket.close();
    }
    */
}