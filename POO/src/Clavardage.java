import java.net.*;

public class Clavardage {
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		NetworkManager nm = new NetworkManager();
		InetAddress adJb = InetAddress.getByName("10.1.5.233");
		System.out.println("print 1");
		if (nm.sendConnectionResquest("jb32", adJb))
			System.out.println("Connexion acceptée");
		else 
			System.out.println("Connexion refusee");
		System.out.println("print 2");
		nm.sendMessage("hello", adJb);
		System.out.println("print 3");
		
		GUI guy = new GUI();
		guy.displayThings();
	}
	
}
