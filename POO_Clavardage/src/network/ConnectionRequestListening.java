package network;

import clavardage.Clavardage;
import clavardage.User;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionRequestListening extends Thread {
    private Clavardage chat;


    public ConnectionRequestListening(Clavardage chat){
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
                String donnees = new String(dataReceived.getData());
                String[] infos = donnees.split(","); //infos[0] = login, infos[1] = Adresse MAC
                String answer = "";
                if (this.chat.getUsers().getLogins().indexOf(infos[0]) != -1)
                    answer = "no";
                else {
                    answer = this.chat.getMe().getLogin();
                    String ipadr = dataReceived.getAddress().toString();
                    User user = new User(infos[0], ipadr, infos[1]);
                    this.chat.addUser(user);
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
