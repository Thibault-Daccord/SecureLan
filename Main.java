import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import SecureLan.CryptSocket;
import SecureLan.Detecteur;
import SecureLan.Serveur;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			if(args[0].equals("s")){
				serveur();
				
			}
			else{
				client();
			}
	    

	}
	
	
	
	static void client() throws Exception{
		System.out.println("lancement du client !");
		Detecteur detecteur= new Detecteur();
		detecteur.lancerRecherche();
		detecteur.actualiserScan();
		ArrayList<Socket> coPossible = detecteur.getConnexionsPossibles();
		System.out.println("le client a trouver "+coPossible.size()+ " serveur");

		CryptSocket cryptSocket = new CryptSocket(coPossible);
		cryptSocket.makeLink();
	}
	
	static void serveur() throws Exception{
		System.out.println("lancement du serveur !");
		Detecteur detecteur= new Detecteur();
		detecteur.lancerRepondeur();// cree un nouveau socket 
		detecteur.actualiserScan();
		ArrayList<Socket> coPossible = detecteur.getConnexionsPossibles();
		System.out.println("le serveur a trouver "+coPossible.size()+ " serveur");
		CryptSocket cryptSocket = new CryptSocket(coPossible);
		cryptSocket.acceptLink();
	}
	

}
