package SecureLan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import SecureLan.cryptage.AESCrypt;
import SecureLan.cryptage.RSACrypt;



/**
 * @author Thibault DACCORD
 *
 */

public class CryptSocket{
	ArrayList<Socket> sockets;
	HashMap<Socket,byte[] > SocketAes = new HashMap<Socket,byte[]>();//socket,cle aes
     SecureLan.cryptage.AESCrypt aes;
     SecureLan.cryptage.RSACrypt rsa;
     boolean cleAesRecu = false;

     boolean chiffrer = true;
     boolean compresser =false ;//a changer
     byte[] cleAes;
	public CryptSocket(Socket socket) {
		sockets = new ArrayList<Socket>();
		sockets.add(socket);
	}
	public CryptSocket(ArrayList<Socket> socketArray) {
		sockets = socketArray;

	}
	
	public CryptSocket(ArrayList<Socket> socketArray,InetAddress ip) {
		sockets = new ArrayList<Socket>();

		for(Socket s : socketArray){
			if(s.getInetAddress()== ip){
				sockets.add(s);
			}
		}
	}	
	
	
	
	public CryptSocket(ArrayList<Socket> socketArray,String ip) {
		for(Socket s : socketArray){
			if(s.getInetAddress().toString().substring(1).equalsIgnoreCase(ip)){
				sockets.add(s);
			}
			
		}	
	}
	

	 /**
	 * @throws Exception
	 * permet de cree une connexion chiffree pour les sockets
	 */
	public void makeLink() throws Exception{
		 for(Socket s : sockets){
			 makeLink1(s);
		 }
	 }
	
	 /**
	 * @param s
	 * @throws Exception
	 * permet de cree une connexion chiffree pour un socket
	 */
	void makeLink1(Socket s) throws Exception{
		 
		DataOutputStream out;
		rsa= new RSACrypt();
		rsa.generateKey();
		PublicKey cleRsaPublic = rsa.getPublicKey();
		PrivateKey cleRsaPrive = rsa.getPrivateKey();
		byte[] q =cleRsaPublic.getEncoded();
		String rep= genererQuestionCleActivation()+"#";
		for(byte b :q){
			rep+=","+b;
		}		
			 out = new DataOutputStream(s.getOutputStream());
	          out.writeUTF(rep);
		DataInputStream in;
		     in = new DataInputStream(s.getInputStream());
		     String questEtcleAesCrypte= in.readUTF();
		     String repActivationRecu=questEtcleAesCrypte.split("#")[0];
		     if(repActivationRecu.equals(this.genererReponseCleActivation())){
			     String cleAesCrypte=questEtcleAesCrypte.split("#")[1];
			     cleAesCrypte=cleAesCrypte.substring(1,cleAesCrypte.length());
			     //System.out.println("la cle aes 1 est "+tabToString(bcleAesCrypte));
			     System.out.println("la cle aes crypte est "+cleAesCrypte);
			     
			     cleAes = rsa.decrypt(StringKeyToByte(cleAesCrypte));
			     			     
			     System.out.println("cle aes");
			     printTab(cleAes);
				  SocketAes.put(s,cleAes);
				 
				out.writeUTF("ok");
		     }
		     else{
		    	 System.out.println("bad rep "+repActivationRecu);
		     }
		
	}
	
	 
	 /**
	 * @throws Exception
	 * accepte les connexions chiffrees
	 */
	public void acceptLink() throws Exception{
		 for(Socket s : sockets){
			 acceptLink(s);
		 }
	 
	 }

	 
	 /**
	 * @param s
	 * @throws Exception
	 * accepte les connexions chiffrees pour un socket
	 */
	private  void acceptLink(Socket s) throws Exception{
		aes = new AESCrypt();
		byte[] cleAesCrypte;
		byte[] cleAes;
		
		cleAes = aes.generateKey();
		System.out.println("la cle aes nom crypté a envoyer est "+tabToString(cleAes));

		byte[] rsaPublicKey;
		String algorithm = "RSA"; // or RSA, DH, etc.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		DataInputStream in = new DataInputStream(s.getInputStream());
		boolean bon =false;
		String rep="";
		String sRsa="";
		while(!bon){
			rep = in.readUTF();
			String[] q=rep.split("#,");
			if(q.length>1){
				String qq=q[0];
				sRsa=q[1];
				
				if(qq.equals(genererQuestionCleActivation()));
					bon=true;
			}
		}
		
		System.out.println("rsa="+sRsa);
		rsaPublicKey=StringKeyToByte(sRsa);
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(rsaPublicKey);
			PublicKey pk = keyFactory.generatePublic(publicKeySpec);

			
			//System.out.println("la cle aes non crypte est "+scleAes);
			cleAesCrypte = rsa.encrypt(cleAes,pk);
			System.out.println("la cle aes crypte est ");
			for(byte b : cleAesCrypte){
				System.out.print(b);
			}
			DataOutputStream out;
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(genererReponseCleActivation()+"#"+tabToString(cleAesCrypte));
			System.out.println("cle aes envoyer");
			 bon =false;
			rep = in.readUTF();
			if(rep.equals("ok")){
				SocketAes.put(s, cleAes);
				System.out.println("socket ajouter");
			}
			else{
				System.out.println("echec de la connexion");
			}
		
		
	}
		
	/**
	 * @param s
	 * @return
	 * transforme la cle sous la forme ",-1,5,..."
	 */
	private byte[] StringKeyToByte(String s){
		String[] tab = s.split(",");
		int cpt=0;
		byte[] tabB = new byte[tab.length];
		Integer inte;
		for(int i=0;i<tab.length;i++){
			inte=Integer.parseInt(tab[i]);
			tabB[i] = inte.byteValue();
		}
		return tabB;
	}
	
	
	
	
	/**
	 * @return
	 * question pour demander l'activation distant d un socket crypte
	 */
	private String genererQuestionCleActivation(){
		String s = "@Questcrypt42SecureLan2";
		return s;	
	}
	
	/**
	 * @return
	 * 	reponse pour la demande d activation distant d un socket crypte

	 */
	private String genererReponseCleActivation(){
		String s = "@Repcrypt42SecureLan2";
		return s;	
	}	
	

	
	
	/**
	 * @throws IOException
	 * ferme les sockets
	 */
	public void fermer() throws IOException{
		for(Socket s: sockets){
			s.close();
		}
		
	}
	
	
	
	
	
	
	/**
	 * @param tab
	 * @throws IOException
	 * ecrit dans les socket un tabeau de byte
	 */
	public void write(byte[] tab) throws IOException{
		DataOutputStream out;
		aes  = new AESCrypt();
		for(Socket s : sockets){
			System.out.println("la cle aes est "+tabToString(SocketAes.get(s)));
			aes.setKey(SocketAes.get(s));
			byte[] msg = aes.encrypt(tab);
			out = new DataOutputStream(s.getOutputStream());
			out.writeInt(msg.length);
			out.write(msg);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * lis dans un socket un tableau de byte
	 */
	public byte[] readFully() throws Exception{
		if(sockets.size()>1){
			throw new Exception("il y a plus d un socket, utiliser la fonction readAllFully ");
		}
		DataInputStream in;
		aes  = new AESCrypt();byte[] tab;
		for(Socket s : sockets){
			aes.setKey(SocketAes.get(s));
			in = new DataInputStream(s.getInputStream());
			tab = new byte[in.readInt()];
			in.readFully(tab);
			tab = aes.decrypt(tab);
			return tab;
		}
		throw new Exception("pas de socket");
	}
	
	/**
	 * @return
	 * @throws IOException
	 * lis dans tous les sockets un tableau de byte
	 */
	public ArrayList<byte[]> readAllFully() throws IOException{
		DataInputStream in;
		ArrayList<byte[]> retour = new ArrayList<byte[]>();
		aes  = new AESCrypt();byte[] tab;
		for(Socket s : sockets){
			aes.setKey(SocketAes.get(s));
			in = new DataInputStream(s.getInputStream());
			tab = new byte[in.readInt()];
			in.readFully(tab);
			tab = aes.decrypt(tab);
			retour.add(tab);
		}
		return retour;
	}
	
	/**
	 * @param msg
	 * @throws IOException
	 * ecrit dans les socket un string
	 */
	public void writeUTF(String msg) throws IOException{
		write(msg.getBytes());
	}
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 *  lis dans un socket un string
	 */
	public String readUTF() throws Exception{
		return new String(readFully());
	}

	
	public void printTab(byte[] tab){
		System.out.println("tab:");
		for(byte b : tab){
			System.out.println(b);
			
		}
		System.out.println("fin tab");
	}
	
	
	
	
	
	/**
	 * @param tab
	 * @return
	 * transforme un tableau de byte en un string lisible
	 */
	public String tabToString(byte[] tab){
		String retour=",";
		for(int i=0;i<tab.length;i++)
		{
			retour+=tab[i]+",";
		}
		return retour;
	}
	
	
	
}
