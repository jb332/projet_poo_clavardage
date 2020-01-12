package network;

import clavardage.Clavardage;
import clavardage.Message;
import clavardage.MessageWay;
import clavardage.User;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MessageReceivingThread extends Thread {
    private Clavardage chat;
    private Socket link;

    public MessageReceivingThread(Socket link, Clavardage chat) {
        this.chat = chat;
        this.link = link;
        super.start();
    }

    public void run() {
        System.out.println("I am a message receiving thread. I have been created to listen to messages coming from " + this.link.getRemoteSocketAddress());
        System.out.println(this.link.getRemoteSocketAddress().getClass());
        String senderIP = ((InetSocketAddress)this.link.getRemoteSocketAddress()).getAddress().toString().replaceFirst("/","");

        User sender = null;
        try {
            sender = this.chat.getUsers().getUserFromIP(senderIP);
        } catch (IllegalArgumentException e) {
            System.out.println("Fatal error : could not get the sender from the IP. Maybe the IP is incorrect.");
            System.out.println(e);
            System.exit(0);
        }

        System.out.println("Sender : " + sender);

        //if no connection had been established (which means this machine is the receiver not the sender) then set the user socket
        if(!sender.socketExists()) {
            sender.setSocket(link);
        }

        System.out.println("Sender socket : " + sender.getSocket());
        Socket socket = sender.getSocket();

        try {
            while (true) {
                DataInputStream input = new DataInputStream(this.link.getInputStream());
                //BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                //String line = reader.readLine();
                String line = input.readUTF();

                System.out.println("Message received : \"" + line + "\"");

                Message receivedMessage = new Message(line, MessageWay.RECEIVED);

                this.chat.treatReceivedMessage(receivedMessage, sender);
            }
        } catch (IOException e) {
            System.out.println("Error : message could not be received : " + e);
        }
    }
}