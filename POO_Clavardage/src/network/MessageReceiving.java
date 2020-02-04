package network;

import clavardage.Clavardage;

import java.io.IOException;
import java.net.*;

/**
 * A thread listening on the message port defined in the network manager for chat messages. It creates other threads to treat received messages.
 */
public class MessageReceiving extends Thread {
    /**
     * The controller.
     */
    private Clavardage chat;

    /**
     * Constructor.
     * @param chat the controller
     */
    protected MessageReceiving(Clavardage chat) {
        this.chat = chat;
        super.start();
    }

    /**
     * Launched by the thread. It listens for messages and create a thread to treat messages received from a user.
     */
    public void run() {
        try {
            ServerSocket servSocket = new ServerSocket(NetworkManager.messagePort);
            System.out.println("Server socket created, listening on port : " + servSocket.getLocalPort());

            Socket link;
            while(true) {
                try {
                    link = servSocket.accept();
                    System.out.println(link.getPort());
                    System.out.println(link.getLocalPort());
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