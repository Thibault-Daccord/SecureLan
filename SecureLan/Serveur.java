package SecureLan;

import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.Thread.currentThread;
import java.util.concurrent.TimeUnit;


 public class Serveur extends Thread {
ArrayList<Socket> connexions = new ArrayList<Socket>();
ArrayList<String> packetIp = new ArrayList<String>();

  public Serveur(){}

  public void run(){

	  try{
	    MulticastSocket socket = null;
	    DatagramPacket inPacket = null;
	    byte[] inBuf = new byte[20]; //port
	    String adresse;
	      //Prepare to join multicast group
	      InetAddress address = InetAddress.getByName("224.2.2.3");//224.2.2.3
	      socket.joinGroup(address);		
	      System.out.println("lancement du serveur");
			long tps0=System.currentTimeMillis();
			long tps1=System.currentTimeMillis();
			boolean bon=true;
			Socket s;
			while(bon){
				tps1=System.currentTimeMillis();  
				if((tps1-tps0)>10000)
					bon=false;
		        inPacket = new DatagramPacket(inBuf, inBuf.length);
		        socket.receive(inPacket);
		        String msg = new String(inBuf, 0, inPacket.getLength());
		        adresse=inPacket.getAddress().toString().substring(1);
		        int port = Integer.parseInt(msg);
		        if(!haveIp(inPacket.getAddress().toString())){
			        System.out.println("creation du socket vers "+inPacket.getAddress()+port);
			        s= new Socket(inPacket.getAddress(), port);
			        connexions.add(s);//todo penser au doublons
			        this.packetIp.add(inPacket.getAddress().toString());
			        System.out.println("adresse ip detecté"+adresse);	
			        
		        }
  
  
  
	      }
	  }catch (Exception e) {
		 System.out.println(e.getMessage());
		System.out.println("erreur dans le serveur");
	}
  }  
	  

	
	public ArrayList<Socket> getConnexions(){
		return connexions;
	}
	
	
    boolean haveIp(String ip){
    	for(String s : packetIp){
    		if(s.equals(ip))
    			return true;
    	}
    	return false;
    }
}
