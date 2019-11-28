import java.net.*;
import java.io.*;
public class NetworkManager {
	
		public NetworkManager() {
			
		}
		final int port_connexion = 10000;
		final int port_message = 20000;
	public boolean connectionResquest(String login) throws Exception {
			//emission de la demande
			byte addbroadcast[] = {127, 127, 127, 127};
			InetAddress broadcast = InetAddress.getByAddress(addbroadcast);
			byte buffer[] = login.getBytes();
			DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, broadcast, port_connexion);
			DatagramSocket socket = new DatagramSocket();
			socket.send(dataSend);
			//réception réponse
			byte bufferReception[] = new byte[10];
			DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
			socket.receive(dataReceived);
			socket.close();
			return dataReceived.getData().toString().equals("ok");
				
		}
	
	public void sendMessage(String message) throws Exception{
		
		byte adddest[] = {127, 127, 127, 127};
		InetAddress adressedest = InetAddress.getByAddress(adddest);
		byte buffer[] = message.getBytes();
		DatagramPacket dataSend = new DatagramPacket(buffer, buffer.length, broadcast, port_connexion);
		Socket socket = new Socket(adressedest, port_message);
		socket.send(dataSend);
		//réception réponse
		byte bufferReception[] = new byte[10];
		DatagramPacket dataReceived = new DatagramPacket(bufferReception, bufferReception.length);
		socket.receive(dataReceived);
		socket.close();
		
		
	}
	
		
	}

