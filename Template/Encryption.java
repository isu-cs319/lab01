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

	public static void main(String[] args) {
		String msg = "Hello, my name is David.";
		byte[] bytes = encryptMessage(msg);
		System.out.println("Encrypted: "+ Encryption.bytesToString(bytes));
		System.out.println("Decrypted: " + Encryption.decryptMessage(bytes));

	}

}
