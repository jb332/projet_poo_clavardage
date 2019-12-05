package clavardage;

import java.io.IOException;
import java.net.*;

public class MessageReceiving extends Thread {
    private Clavardage chat;
    private final Integer portNumber = 1024;

    public MessageReceiving(Clavardage chat) {
        this.chat = chat;
        super.start();
    }

    public void run() {
        try {
            ServerSocket servSocket = new ServerSocket(portNumber);

            Socket link;
            while(true) {
                try {
                    link = servSocket.accept();
                    new MessageReceivingThread(link, chat);
                } catch (Exception e) {
                    System.out.println("Error : " + e);
                }
            }
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
