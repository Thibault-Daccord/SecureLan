package SecureLan.broadcast;
import java.io.*;
import java.net.*;
import java.util.*;
 public class QuiEstLa extends Thread {
 String portVouluL;
 String message;
  public QuiEstLa(String message){
    this.message=message;
  }
  
  public void run(){
  DatagramSocket socket = null;
    DatagramPacket outPacket = null;
    byte[] outBuf;
    Socket socketReponse;
	try{
    try {
      socket = new DatagramSocket();
      int counter = 0;
      while (true) {
		//msgMesIp=""; 
		//MesIp=getId();
		//for (String[] s: MesIp){
			//msgMesIp=msgMesIp+","+s[1];
		//}		
        counter++;
        outBuf = message.getBytes();
        //Send to multicast IP address and port
        InetAddress address = InetAddress.getByName("224.2.2.3");//224.2.2.3
        //System.out.println(address);
        // for (byte b :outBuf){
			//System.out.println(""+outBuf.length+"outBuf"+b);
		//}
        outPacket = new DatagramPacket(outBuf, outBuf.length, address,8888);
        socket.send(outPacket);
        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
        }
      }
    } catch (IOException ioe) {
      }
      } catch (Exception e) {System.out.println("erreur lors de l obsention de l adressse ip");}
    }


      
}
