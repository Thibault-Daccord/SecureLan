package SecureLan.cryptage;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//@author charles Thibaud, thibault Daccord
public class AESCrypt {

	private  final String IV = "AAAAAAAAAAAAAAAQ";

	private byte[] encryptionKey ;//doit faire 16 caracteres



  public  byte[] encrypt(byte[] donnees) {
	  byte[] encrypted = null;
	  try{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
	 encrypted= cipher.doFinal(donnees);
	  }catch(Exception e){e.printStackTrace();}
	  return encrypted;
  }

  public byte[] decrypt(byte[] cipherText) {
	    byte[] decrypted = {};
	  try{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    decrypted =  cipher.doFinal(cipherText);
		}catch(Exception e){e.printStackTrace();}
	return decrypted;
  }

  public void setKey(byte[] key)
  {
	  this.encryptionKey = key;
  }


  public byte[] generateKey()
  {
	SecretKey secretKey = null;
	try{
	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	keyGen.init(128);
	secretKey = keyGen.generateKey();
	encryptionKey=secretKey.getEncoded();

	}catch(Exception e){e.printStackTrace();}
	return encryptionKey;
  }

  public byte[] getKey()
  {
	  return encryptionKey;
  }
}
