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

            String senderIP = this.link.getRemoteSocketAddress().toString();
            User sender = this.chat.getUserFromIP(senderIP);
            User receiver = this.chat.getMe();
            Message receivedMessage = new Message(line, sender, receiver);

            this.chat.storeReceivedMessage(receivedMessage);
            ObjectOutputStream oos = new ObjectOutputStream(this.link.getOutputStream());

            oos.writeObject("test message");
            oos.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
