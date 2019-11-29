import java.net.*;
import java.io.*;
public class NetworkManager {
	
	
		public NetworkManager() {
			
		}
		final int port_connexion = 10000;
		final int port_message = 20000;
	public boolean sendConnectionResquest(String login, InetAddress addbroadcast) throws Exception {
			//emission de la demande

			byte buffer[] = login.getBytes();
			DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, addbroadcast, port_connexion);
			DatagramSocket socket = new DatagramSocket(port_connexion);
			socket.send(dataSend);
			//réception réponse
			byte bufferReception[] = new byte[10];
			DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
			socket.receive(dataReceived);
			socket.close();
			return dataReceived.getData().toString().equals("ok");
				
		}
	
	public void sendMessage(String message, InetAddress adressedest) throws Exception{
		
		Socket socket = new Socket(adressedest, port_message);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); 
		oos.writeObject(message);
		oos.close();
		socket.close();
	
	}
	
	public String receiveMessage(ServerSocket servSocket) throws Exception {
		Socket link = servSocket.accept();
		ObjectInputStream ios = new ObjectInputStream(link.getInputStream());
		String message = (String) ios.readObject();
		link.close();
		return message;
		
	}
	
	public String listenMessage() throws Exception {
		ServerSocket servSocket = new ServerSocket(port_message);
		return receiveMessage(servSocket);
	}
	public void receiveConnexionRequest(String mylogin) throws Exception {
		
		
		byte bufferReception[] = new byte[10];
		DatagramSocket socket = new DatagramSocket(port_connexion);
		DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
		socket.receive(dataReceived);
		String login = dataReceived.getData().toString();
		String answer = "";
		if (login.equals(mylogin))
			answer = "no";
		else
			answer = "ok";
		
		byte buffer[] = answer.getBytes();
		DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, dataReceived.getAddress(), port_connexion);
		
		socket.send(dataSend);
		//réception réponse

		socket.close();
		
	}
}

