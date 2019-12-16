package clavardage;

import java.io.*;
import java.net.Socket;

public class MessageReceivingThread extends Thread {
    private Clavardage chat;
    private Socket link;

    public MessageReceivingThread(Socket link, Clavardage chat) {
        this.link = link;
        super.start();
    }

    public void run() {
        try {
            InputStream input = this.link.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = reader.readLine();
            System.out.println(line);

            String senderIP = this.link.getRemoteSocketAddress().toString();
            // User sender = this.chat.getUserFromIP(senderIP);
            //User receiver = this.chat.getMe();
            Message receivedMessage = new Message(line, MessageWay.RECEIVED);

            this.chat.storeReceivedMessage(receivedMessage, new User("toto", "", ""));

        } catch(IOException e) {
            System.out.println(e);
        }
    }
}