package clavardage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionRequestListeningThread extends Thread {
	private DatagramSocket link;
	private Clavardage chat;
	
	
    public ConnectionRequestListeningThread(DatagramSocket link, Clavardage chat){
    	this.link = link;
    	this.chat = chat;
        super.start();
    }
    
    public void run() {
    	
    	try {
        byte bufferReception[] = new byte[10];
        DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
        link.receive(dataReceived);
        String login = dataReceived.getData().toString();
        String answer = "";
        if (this.chat.getConnectedUsersHistory().getLogins().indexOf(login) != -1)
            answer = "no";
        else 
            answer = this.chat.getMe().getLogin();

        byte buffer[] = answer.getBytes();
        DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, dataReceived.getAddress(), NetworkManager.port_connexion);
        DatagramSocket answerLink = new DatagramSocket();
        answerLink.send(dataSend);
        //réception réponse

        link.close();
        answerLink.close();
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    	
    }
}
