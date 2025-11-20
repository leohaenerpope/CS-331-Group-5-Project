import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Scanner;

/**
 * 
 */
public class AESTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for input
        System.out.print("Enter text to encrypt: ");
        String input = scanner.nextLine();

        // Generate AES key
        SecretKey aesKey = generateAESKey();

        // Encrypt using helper method
        String encryptedText = encrypt(input, aesKey);
        System.out.println("Original: " + input);
        System.out.println("Encrypted (Base64): " + encryptedText);

        // Decrypt back to verify round-trip
        String decryptedText = decrypt(encryptedText, aesKey);
        System.out.println("Decrypted: " + decryptedText);

        scanner.close();
    }

    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit key size
        return keyGen.generateKey();
        
    }

    public static String encrypt(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
