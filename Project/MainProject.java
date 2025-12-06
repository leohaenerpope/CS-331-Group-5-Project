package Project;

import java.util.Scanner;

public class MainProject {

    public static void main(String[] args) {
        int demoLevel = 0;

        Scanner scanner = new Scanner(System.in);

        if(args.length == 1){
            if(Integer.parseInt(args[0]) == 1){
                demoLevel = 1;
            }
        }

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

            //Encryption and decryption with print statements to walk through AES steps
            if(demoLevel == 0){
                // Encrypt
                String encryptedText = aes.encrypt(plaintext, key, demoLevel);
                

                // Decrypt
                String decryptedText = aes.decrypt(encryptedText, key, demoLevel);
                

                System.out.println();
                System.out.println("Encrypted text:");
                System.out.println(encryptedText);

                System.out.println();
                System.out.println("Decrypted text:");
                System.out.println(decryptedText);

                System.out.println();
                System.out.println("Decryption matches original: " + plaintext.equals(decryptedText));
            }

            //Encryption and decryption
            else{
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
            }

        } finally {
            scanner.close();
        }
    }
}
