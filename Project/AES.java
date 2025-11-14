package Project;
import Jama.Matrix;
// Below is the link that describe what methods there are and what they do
// https://biojava.org/docs/api/org/biojava/nbio/structure/jama/Matrix.html

public class AES {
    private int roundNum;
    private String key;
    private int keyLength;
    private Matrix dataMatrix;
    
    public AES() {

    }

    public String encrypt(String data, String key) {
        // Placeholder for AES encryption logic
        return "encryptedData";
    }

    public String decrypt(String encryptedData, String key) {
        // Placeholder for AES decryption logic
        return "decryptedData";
    }

    private void ShiftRows() {
        // Placeholder for ShiftRows logic
    }

    private void MixColumns() {
        // Placeholder for MixColumns logic
    }

    private void ByteSubstitution() {
        // Placeholder for ByteSubstitution logic
    }

    private void AddRoundKey() {
        // Placeholder for AddRoundKey logic
    }
}
