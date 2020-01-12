package network;

import clavardage.Clavardage;
import clavardage.Message;
import clavardage.User;
import network.ConnectionRequestListening;
import network.MessageReceiving;
import network.MessageReceivingThread;

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
        new ConnectionRequestListening(chat);
        //create and launch a thread to store incoming messages
        new MessageReceiving(chat);
    }

    public boolean connectAndCheckLogin(String login) {
        try {
            return sendConnectionResquest(login, InetAddress.getByName("255.255.255.255"));
        } catch(Exception e) {
            System.out.println("Could not send connection request : ");
            System.out.println(e);
            return false;
        }
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

    public void sendMessage(Message message, User receiver) throws Exception {
        //check if connection already exists
        System.out.println("Currently trying to send a message to " + InetAddress.getByName(receiver.getIpAddress()));

        Socket socket;
        if(receiver.socketExists()) {
            //a connection already exists with this user so we just use the saved socket
            socket = receiver.getSocket();

            System.out.println("A socket already exists : " + socket);
        } else {
            //it is the first time a message is exchanged with this user so we create a socket and we save it
            InetAddress destAddr = InetAddress.getByName(receiver.getIpAddress());
            socket = new Socket(destAddr, port_message);
            receiver.setSocket(socket);

            System.out.println("Socket created : " + socket);

            //we create a thread to listen on this socket because the receiver will respond with this socket
            new MessageReceivingThread(socket, this.chat);
        }
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        //oos.writeChars(message.getContent());
        oos.writeUTF(message.getContent());
        //oos.close();
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