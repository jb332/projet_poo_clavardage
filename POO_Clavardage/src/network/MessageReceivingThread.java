package network;

import clavardage.Clavardage;
import clavardage.Message;
import clavardage.User;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * A thread used to receive messages from a specific user.
 */
public class MessageReceivingThread extends Thread {
    /**
     * The controller.
     */
    private Clavardage chat;
    /**
     * The TCP socket used for communication with a user.
     */
    private Socket link;

    /**
     * Constructor.
     * @param link the TCP client socket
     * @param chat the controller
     */
    protected MessageReceivingThread(Socket link, Clavardage chat) {
        this.chat = chat;
        this.link = link;
        super.start();
    }

    /**
     * The method launched by the thread. It receives and treats messages.
     */
    public void run() {
        System.out.println("I am a message receiving thread. I have been created to listen to messages coming from " + this.link.getRemoteSocketAddress());
        InetAddress senderIP = ((InetSocketAddress)this.link.getRemoteSocketAddress()).getAddress();

        User sender = null;
        try {
            sender = this.chat.getUsers().getUserFromIP(senderIP);
        } catch (IllegalArgumentException e) {
            System.out.println("Fatal error : could not get the sender from the IP. Maybe the IP is incorrect : " + e);
            System.exit(1);
        }

        System.out.println("Sender : " + sender);

        //if no connection had been established (which means this machine is the receiver not the sender) then set the user socket
        if(!sender.socketExists()) {
            sender.setSocket(link);
        }

        Socket socket = sender.getSocket();

        try {
            while (true) {
                DataInputStream input = new DataInputStream(this.link.getInputStream());
                String content = input.readUTF();

                System.out.println("Message received : \"" + content + "\"");

                Message receivedMessage = new Message(content, Message.RECEIVED);

                this.chat.treatReceivedMessage(receivedMessage, sender);
            }
        } catch (SocketException e) {
            System.out.println("Socket closed for user : " + sender.getLogin() + " : " + e);
        } catch (IOException e) {
            System.out.println("Error : message could not be received : " + e);
        }
    }
}