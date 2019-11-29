import java.net.*;

public class Clavardage {
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		NetworkManager nm = new NetworkManager();
		InetAddress adJb = InetAddress.getByName("10.32.2.63");
		if (nm.sendConnectionResquest("jb32", adJb))
			System.out.println("Connexion acceptée");
		else 
			System.out.println("Connexion refusee");
		
		nm.sendMessage("hello", adJb);
		
		GUI guy = new GUI();
		guy.displayThings();
	}
	
}
