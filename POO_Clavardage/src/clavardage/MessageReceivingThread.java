package clavardage;

import java.io.*;
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
        try {
            String senderIP = this.link.getRemoteSocketAddress().toString();
            User sender = this.chat.getUsers().getUserFromIP(senderIP);
            //if no connection had been established (which means this machine is the receiver not the sender) then set the user socket
            if(!sender.socketExists()) {
                sender.setSocket(link);
            }

            while (true) {
                InputStream input = this.link.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();
                System.out.println(line);

                Message receivedMessage = new Message(line, MessageWay.RECEIVED);

                this.chat.treatReceivedMessage(receivedMessage, sender);
            }
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}