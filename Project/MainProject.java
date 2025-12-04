package Project;

import java.util.Scanner;

/**
 * Main driver for the AES demo.
 * Handles user input, calls AES.encrypt / AES.decrypt,
 * and prints results.
 */
public class MainProject {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AES aes = new AES();

        System.out.println("==== CS-331 AES Demo ====");
        System.out.println("Choose mode :");
        System.out.println("  [E] Encrypt  ");
        System.out.println("  [D] Decrypt  ");
        System.out.print("Enter choice (E/D): ");

        String mode = scanner.nextLine().trim().toUpperCase();

        if (!mode.equals("E") && !mode.equals("D")) {
            System.out.println("Invalid choice. Please run again with E or D.");
            scanner.close();
            return;
        }

        // Obtain a 128-bit key (16 characters)
        System.out.print("Enter 16-character key (128-bit): ");
        String key = scanner.nextLine();

        if (key.length() != 16) {
            System.out.println("WARNING: key should be exactly 16 characters (128 bits).");
            System.out.println("The recommended key length is 16 characters (128 bits); otherwise, KeyManager may issue a warning.");
        }

        try {
            if (mode.equals("E")) {
                // 加密
                System.out.print("Enter plaintext to encrypt : ");
                String plaintext = scanner.nextLine();

                String ciphertextBase64 = aes.encrypt(plaintext, key);
                System.out.println("\n=== Encryption Result  ===");
                System.out.println("Ciphertext (Base64):");
                System.out.println(ciphertextBase64);

            } else {
                // 解密
                System.out.print("Enter Base64 ciphertext to decrypt : ");
                String ciphertextBase64 = scanner.nextLine().trim();

                String decrypted = aes.decrypt(ciphertextBase64, key);
                System.out.println("\n=== Decryption Result  ===");
                System.out.println("Plaintext :");
                System.out.println(decrypted);
            }
        } catch (Exception e) {
            System.out.println("Error during AES operation :");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
