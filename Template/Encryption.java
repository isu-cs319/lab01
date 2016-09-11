package Template;

public class Encryption {
	
	public static String decryptMessage(byte[] bytes) {
		byte[] bytesDecrypted = new byte[bytes.length];
		  for (int i = 0; i < bytes.length; i++)
		  {
			  bytesDecrypted[i] = (byte) (bytes[i] ^ 11110000);
		  }
		  return Encryption.bytesToString(bytesDecrypted);
	}
	
	public static String bytesToString(byte[] bytes){
		return new String(bytes);
	}
	
	public static byte[] encryptMessage(String txt){
		byte[] bytes = txt.getBytes();
		byte[] bytesEncrypted = new byte[bytes.length];
		  for (int i = 0; i < bytes.length; i++)
		  {
			  bytesEncrypted[i] = (byte) (bytes[i] ^ 11110000);
		  }
		  return bytesEncrypted;
	}
	
	public static String decryptImage(byte[] img){
		byte[] decrypted = new byte[img.length-2];
		for (int i = 2; i < img.length; i++){
			decrypted[i-2] = img[i];
		}
		return bytesToString(decrypted);
	}

	public static void main(String[] args) {

	}

}
