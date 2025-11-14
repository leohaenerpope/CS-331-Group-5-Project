package Project;
import Jama.Matrix;
// Below is the link that describe what methods there are and what they do
// https://biojava.org/docs/api/org/biojava/nbio/structure/jama/Matrix.html

public class AES {
    private int roundNum;
    private String key;
    private int keyLength;
    private double[][] dataArray;
    private Matrix mixedColumnMatrix;
    private Matrix dataMatrix;
    
    public AES() {
        this.roundNum = 10;
        this.key = "";
        this.keyLength = 128;
        this.dataArray = new double[4][4];
        this.mixedColumnMatrix = new Matrix(new double[][]{
            {2, 3, 1, 1},
            {1, 2, 3, 1},
            {1, 1, 2, 3},
            {3, 1, 1, 2}
        });
        this.dataMatrix = new Matrix(dataArray);
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
        dataArray = dataMatrix.getArray();
        for (int i = 1; i < dataArray.length; i++) {
            double[] newRow = new double[dataArray[i].length];
            for (int j = 0; j < newRow.length; j++) {
                newRow[j] = dataArray[i][(j + i) % newRow.length];
            }
            dataArray[i] = newRow;
        }
        dataMatrix = new Matrix(dataArray);
    }

    private void MixColumns() {
        dataMatrix = mixedColumnMatrix.times(dataMatrix);
        dataArray = dataMatrix.getArray();
    }

    private void ByteSubstitution() {
        // Placeholder for ByteSubstitution logic
    }

    private void AddRoundKey() {
        // Placeholder for AddRoundKey logic
    }
}
