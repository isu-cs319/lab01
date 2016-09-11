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

		public static byte[] encryptImage(String txt) {
		byte[] bytes = txt.getBytes();
		byte[] bytesEncrypted = new byte[bytes.length];
		
		
		for (int i = 0; i < bytes.length; i+=3) {
			byte[] tmp = new byte[3];
			tmp[0] = bytes[i];
			tmp[1] = bytes[i+1];
			tmp[2] = bytes[i+2];

			String string = "";
			string += Encryption.bytesToString(tmp);
			
			String[] strarr = new String[4];
			strarr[0] = "00" + string.substring(0, 6);
			strarr[1] = "00" + string.substring(6, 12);
			strarr[2] = "00" + string.substring(12, 18);
			strarr[3] = "00" + string.substring(18, 24);
						
			for (int j=0; j<4; j++) {
				bytesEncrypted[i] = (byte) Integer.parseInt(strarr[i]);
			}
			
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
