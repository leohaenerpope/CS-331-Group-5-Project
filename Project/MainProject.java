package Project;

import java.util.Scanner;

public class MainProject {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("=== AES Encryption / Decryption Demo ===");
            System.out.print("Enter the message to encrypt: ");
            String plaintext = scanner.nextLine();

            String key;
            while (true) {
                System.out.print("Enter a key (exactly 16 characters for 128-bit AES): ");
                key = scanner.nextLine();

                if (key.length() == 16) {
                    break;
                } else {
                    System.out.println("Key must be exactly 16 characters. You entered length: " + key.length());
                }
            }
            AES aes = new AES();

            // Encrypt
            String encryptedText = aes.encrypt(plaintext, key);
            System.out.println();
            System.out.println("Encrypted text:");
            System.out.println(encryptedText);

            // Decrypt
            String decryptedText = aes.decrypt(encryptedText, key);
            System.out.println();
            System.out.println("Decrypted text:");
            System.out.println(decryptedText);

            System.out.println();
            System.out.println("Decryption matches original: " + plaintext.equals(decryptedText));

        } finally {
            scanner.close();
        }
    }
}
