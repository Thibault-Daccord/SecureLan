package SecureLan;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import SecureLan.broadcast.*;


public class Detecteur {
	ArrayList<Socket> connexionsPossibles = new ArrayList<Socket>();
	Client client=null;
	Serveur serveur=null;
	QuiEstLa quiEstLa;
	public Detecteur() {
	}
	

	
	
	public void lancerRepondeur(){
	    serveur = new Serveur();
		serveur.run();
	}
	
	
	
	public void lancerRecherche() throws InterruptedException, IOException {
		int port =3000;
		ServerSocket socketServeur ;
		boolean portOk=false;
		try{
			while (!portOk){
				try {
			     socketServeur = new ServerSocket(port);
			     socketServeur.close();
				 portOk=true;
				}catch(Exception E){
				port+=1;
				if (port>9999){
					System.out.println("Merci de vous mettre en root car tout les ports sont occupés");
					 port=0;
					 
				}	
				}
			}
		}catch(Exception e){}
		quiEstLa = new QuiEstLa(""+port);
		quiEstLa.start();
		client = new Client(port);
		client.recherche();
		System.out.println("recherche en cour");

	}
	
	
	
	public void actualiserScan() throws InterruptedException{
		System.out.println("actualiserScan");
		connexionsPossibles = new ArrayList<Socket>();
		TimeUnit.MILLISECONDS.sleep(300);
		if(client!=null)
			connexionsPossibles.addAll(client.getConnexions());
		if(serveur!=null)
			connexionsPossibles.addAll(serveur.getConnexions());
    }	
	
	public ArrayList<Socket> getConnexionsPossibles() throws InterruptedException{
		actualiserScan();
		System.out.println("il y a "+connexionsPossibles.size()+" co possible");
		return connexionsPossibles;
	}
	
	
	public void stoperRecherche(){
		quiEstLa.interrupt();
	}
	

	
	}
	
