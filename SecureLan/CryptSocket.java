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
		// TODO Auto-generated constructor stub
	}
	public CryptSocket(ArrayList<Socket> socketArray) {
		sockets = socketArray;

		// TODO Auto-generated constructor stub
	}
	public CryptSocket(ArrayList<Socket> socketArray,InetAddress ip) {
		sockets = new ArrayList<Socket>();

		for(Socket s : socketArray){
			if(s.getInetAddress()== ip){
				sockets.add(s);
			}
		}
		// TODO Auto-generated constructor stub
	}	
	
	public CryptSocket(ArrayList<Socket> socketArray,String ip) {
		for(Socket s : socketArray){
			if(s.getInetAddress().toString().substring(1).equalsIgnoreCase(ip)){
				sockets.add(s);
				System.out.println("ip add :"+ip);
			}
			
		}	
	}
	
	/*/
	private void encrypt() throws Exception{
		DataInputStream in;
		byte[] buf = new byte[genererReponseCleActivation().length];
		for(Socket s :sockets){
		     in = new DataInputStream(s.getInputStream());
			 in.read(buf);
			 if(!equals(buf,genererReponseCleActivation())){
				 makeLink(s);
			 }
			 else{
				 acceptLink(s);
			 }
		}	
		
	}
	/*/
	
	 public void makeLink() throws Exception{
		 for(Socket s : sockets){
			 makeLink1(s);
		 }
	 }
	
	 void makeLink1(Socket s) throws Exception{
		 
		DataOutputStream out;
		rsa= new RSACrypt();
		rsa.generateKey();
		PublicKey cleRsaPublic = rsa.getPublicKey();
		PrivateKey cleRsaPrive = rsa.getPrivateKey();
		byte[] q =cleRsaPublic.getEncoded();
		String rep= genererQuestionCleActivation()+"#";
		System.out.println("reep="+rep);
		for(byte b :q){
			System.out.println(b);
			rep+=","+b;
		}
		System.out.println("rep="+rep);
		
		System.out.println("la cle cleRsaPublic  a une taille de "+cleRsaPublic.getEncoded().length);
			 out = new DataOutputStream(s.getOutputStream());
	          out.writeUTF(rep);
		DataInputStream in;
		     in = new DataInputStream(s.getInputStream());
		     String questEtcleAesCrypte= in.readUTF();
		     String repActivationRecu=questEtcleAesCrypte.split("#")[0];
		     if(repActivationRecu.equals(this.genererReponseCleActivation())){
			     String cleAesCrypte=questEtcleAesCrypte.split("#")[1];
			     cleAesCrypte=cleAesCrypte.substring(1,cleAesCrypte.length());
			     byte[] bcleAesCrypte=StringKeyToByte(cleAesCrypte);
			     cleAes=rsa.decrypt(bcleAesCrypte);
			     System.out.println(cleAesCrypte);
			     System.out.println(cleAes);
			     System.out.println(s);
				  SocketAes.put(s,cleAes);
				 
				System.out.println("la cle aes recu est "+tabToString(cleAes));
				out.writeUTF("ok");
		     }
		     else{
		    	 System.out.println("bad rep "+repActivationRecu);
		     }
		
	}
	
	 
	 public void acceptLink() throws Exception{
		 for(Socket s : sockets){
			 acceptLink(s);
		 }
	 
	 }

	 
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
				cleAesCrypte = rsa.encrypt(cleAes,pk);
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
	
	
	
	private byte[] concat(byte[] a,byte[] b){
		byte[] tab = new byte[a.length+b.length];
		int cpt=0;
		for(cpt=0;cpt<a.length;cpt++){
			tab[cpt] = a[cpt];
		}
		for(int i=cpt;i<b.length;i++){
			cpt++;
			tab[cpt]= b[i];
		}
		return tab;
		
		
	}
	
	
	private String genererQuestionCleActivation(){
		String s = "@Questcrypt42SecureLan2";
		return s;	
	}
	
	private String genererReponseCleActivation(){
		String s = "@Repcrypt42SecureLan2";
		return s;	
	}	
	
	private boolean equals(byte[] a,byte b[]){
		int lg = a.length;
		if(lg!=b.length)
			return false;
		for(int i =0;i<lg;i++){
			if(a[i]!=b[i]){
				return false;
			}
		}
		return true;
		
	}
	
	
	private byte[] subByte(byte[] tab ,int a,int b){
		byte[] retour = new byte[b-a];
		for(int i=0;i<retour.length;i++){
			retour[i] = tab[a];
			a++;
		}
		return retour;
		
	}
	
	public void fermer() throws IOException{
		for(Socket s: sockets){
			s.close();
		}
		
	}
	
	
	
	
	
	
	public void write(byte[] tab) throws IOException{
		DataOutputStream out;
		aes  = new AESCrypt();
		for(Socket s : sockets){
			aes.setKey(SocketAes.get(s));
			byte[] msg = aes.encrypt(tab);
			out = new DataOutputStream(s.getOutputStream());
			out.writeInt(msg.length);
			out.write(msg);
		}
	}

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
	
	public void writeUTF(String msg) throws IOException{
		write(msg.getBytes());
	}
	public String readUTF(String msg) throws Exception{
		return new String(readFully());
	}

	
	
	
	
	public  int available(Socket socket) throws IOException{
		DataInputStream in;
		in = new DataInputStream(socket.getInputStream());
		return in.available();
	}	
	
	
	
	
	
	
	public String tabToString(byte[] tab){
		String retour="";
		for(int i=0;i<tab.length;i++)
		{
			retour+=tab[i];
		}
		return retour;
	}
	
	
	
}
