package SecureLan.cryptage;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.*;
/**
 * @author JavaDigest
 *
 */
public class RSACrypt{

  /**
   * String to hold name of the encryption algorithm.
   */
  public static final String ALGORITHM = "RSA";
	public static final String CYPHERINIT = "RSA/ECB/PKCS1Padding";
public static final int KEYLENGTH=1024 ;
	private KeyPairGenerator keyGen;
	private KeyPair key;

  /**
   * Generate key which contains a pair of private and public key using 1024
   * bytes. Store the set of keys in Prvate.key and Public.key files.
   *
   * @throws NoSuchAlgorithmException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public  void generateKey() {
	  try{
     keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(KEYLENGTH);
     key = keyGen.generateKeyPair();
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }


  }


  /**
   * Encrypt the plain text using public key.
   *
   * @param text
   *          : original plain text
   * @param key
   *          :The public key
   * @return Encrypted text
   * @throws java.lang.Exception
   */
  public static byte[] encrypt(byte[] text,PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(text);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.print("erreur dans l encryption");

      System.out.print(e);
    }
    return cipherText;
  }



  /**
   * Decrypt text using private key.
   *
   * @param text
   *          :encrypted text
   * @param key
   *          :The private key
   * @return plain text
   * @throws java.lang.Exception
   */
  public  byte[] decrypt(byte[] text) {
	PrivateKey key = getPrivateKey();
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(CYPHERINIT);

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return dectyptedText;
  }

  public PublicKey  getPublicKey()
  {
	 // System.out.println("la cle public fait "+key.getPublic().getEncoded().length+" de lg");
	return key.getPublic();
  }

  public PrivateKey getPrivateKey()
  {
	return key.getPrivate();
  }


}
