package SecureLan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import SecureLan.broadcast.QuiEstLa;

public class Client {
	ArrayList<Socket> connexions = new ArrayList<Socket>();

	ServerSocket socketServeur;
	int port;
	public Client(int port) throws IOException {
		this.port=port;
		// TODO Auto-generated constructor stub
				
	}

	public void recherche() throws IOException{
		Socket socketClient = null;
		socketServeur =  new ServerSocket(port);
		long tps0=System.currentTimeMillis();
		long tps1=System.currentTimeMillis();
		boolean bon=true;
		while(bon){
			tps1=System.currentTimeMillis();
			if((tps1-tps0)>5000)
				bon=false;
			try{
			socketServeur.setSoTimeout(44);
			socketClient = socketServeur.accept();

			}catch (Exception e) {
				// TODO: handle exception
			}
			if(socketClient!=null)
			if(!haveIp(socketClient.getInetAddress().toString())){
				System.out.println("connexion trouvee :"+ socketClient.getInetAddress());
				connexions.add(socketClient);}
		}

	}
	
	public ArrayList<Socket> getConnexions(){
		return connexions;
	}
	
    boolean haveIp(String ip){
    	for(Socket s : connexions){
    		if(s.getInetAddress().toString().equals(ip))
    			return true;
    	}
    	return false;
    }
	
}
