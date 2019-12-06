package clavardage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionRequestListeningThread extends Thread {
	private Clavardage chat;
	
	
    public ConnectionRequestListeningThread(Clavardage chat){
    	this.chat = chat;
        super.start();
    }
    
    public void run() {
    	try {
    	DatagramSocket link = new DatagramSocket(NetworkManager.port_connexion);
    	while(true) {
        byte bufferReception[] = new byte[10];
        DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
        link.receive(dataReceived);
        String donnees = dataReceived.getData().toString();
        String[] infos = donnees.split(","); //infos[0] = login, infos[1] = Adresse MAC
        String answer = "";
        if (this.chat.getConnectedUsersHistory().getLogins().indexOf(infos[0]) != -1)
            answer = "no";
        else {
            answer = this.chat.getMe().getLogin();
            String ipadr = dataReceived.getAddress().toString();
            User user = new User(infos[0], ipadr, infos[1]);
            this.chat.storeNewUser(user);
        }
        byte buffer[] = answer.getBytes();
        DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, dataReceived.getAddress(), NetworkManager.port_reponse_connexion);
        DatagramSocket answerLink = new DatagramSocket();
        answerLink.send(dataSend);
        //réception réponse

        
        answerLink.close();
    	}
    	
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    	   
    }
    
}
