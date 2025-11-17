package Project;

import java.nio.charset.StandardCharsets;

/**
 * This is the key manager for AES, this can generate and store the round keys
 * for a given (128-bit) key.
 * 
 * Using 128 bit key.
 * 
 * For this I just used byte[] arrays for the key byte values.
 * Should be pretty easy to convert them into any other variable to represent them if we need to.
 * 
 * Java bytes:
 * byte variableName = 10 ----- byte of 10: binary value is 00001010
 * 
 * @author Leo
 */
public class KeyManager {
    private byte[] key; // byte array used to store the key. Should have a length of 16.
    private int keySize = 128;
    private int stringKeySize = keySize / 8;
    private byte[][] roundKeys;

    /**
     * Constructor for making a KeyManager with a non-specified key string.
     * 
     * Uses a pre-determined key string of length 16. (16 chars = 16 * 8 bytes per char = 128 bit size)
     */
    public KeyManager() {
        this.key = getByteKeyFromString("0123456789abcdef"); // THIS MUST BE A STRING OF LENGTH 32
        this.roundKeys = generateRoundKeys(this.key);
    }

    /**
     * Constructor for making a KeyManager with a specified key string.
     * 
     * @param keyString a string of length 16 to be used as the key (16 chars = 16 * 8 bytes per char = 128 bit size)
     */
    public KeyManager(String keyString){
        // Make sure that the size of the string is correct. If not, either cut it or add characters.
        keyString = setCorrectStringKeySize(keyString);
        this.key = getByteKeyFromString(keyString);
        this.roundKeys = generateRoundKeys(this.key);
    }

    /**
     * Creates an 11-length (Rounds 0 through 10) array of 16-length byte arrays
     * Individual round keys are to be used in the key addition step of AES
     * 
     * @param key Base 16-byte length key to be used
     * @return The full array of 16-length byte arrays, which each are round keys, specific to their index in the array
     * (index 0 is the 0th round key, index 10 is the 10th round key)
     */
    private byte[][] generateRoundKeys(byte[] key){
        byte[][] retArr = new byte[11][]; // return array. Stores 11 round keys K0-K10 (10 rounds for 128 bit key)
        retArr[0] = key;
        for (int i = 1; i <= 10; i++){
            retArr[i] = keyScheduleStep(retArr[i-1], i);
        }
        return retArr;
    }

    /**
     * A full step for the key scheduling method used to generate round keys.
     * See Key addition and key schedule in AESPDF.pdf
     * 
     * @param startKey The base 16-byte long key to start off with
     * @param round The current round key that is being made. Used in the G Function -> Round Coefficient Function
     * @return The new round key after all necessary steps were done to the startKey
     */
    private byte[] keyScheduleStep(byte[] startKey, int round){
        byte[][] W = new byte[4][4]; // W Array containing four 4-byte-long arrays - see Key addition and key scheduling in AESPDF.pdf
        for (int i = 0; i < 4; i++){
            W[i] = new byte[] {
                startKey[i * 4],
                startKey[i * 4 + 1],
                startKey[i * 4 + 2],
                startKey[i * 4 + 3]
            };
        }
        W[0] = keyScheduleXOR(W[0], keyScheduleGFunction(W[3], round));
        W[1] = keyScheduleXOR(W[0], W[1]);
        W[2] = keyScheduleXOR(W[1], W[2]);
        W[3] = keyScheduleXOR(W[2], W[3]);

        byte[] retArr = new byte[16]; // return array
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                retArr[i * 4 + j] = W[i][j];
            }
        }
        return retArr;
    }

    /**
     * Used when key scheduling, XORs two different byte arrays, both of length 4
     * Individually XORs each specific spot in the 2 byte arrays
     * 
     * @param first First byte array to be XORed
     * @param second Second byte array to be XORed
     * @return New byte array of 4 length after first and second were XORed
     */
    private byte[] keyScheduleXOR(byte[] first, byte[] second){
        byte[] retArr = new byte[4]; // return array
        for (int i = 0; i < 4; i++) {
            retArr[i] = (byte) (first[i] ^ second[i]);
        }
        return retArr;
    }

    /**
     * The G function for key scheduling, used to determine new first 4-byte long W[0]
     * See Key addition and key schedule in AESPDF.pdf
     * 
     * @param inputBytes The 4 bytes to be put through the G Function
     * @param round The current round of key scheduling, necessary for round coefficient function
     * @return New set of 4 bytes after G Function applied
     */
    private byte[] keyScheduleGFunction(byte[] inputBytes, int round){
        byte[] retArr = new byte[4]; // return array

        // Shift bytes to the left
        for (int i = 0; i < 4; i++){
            if (i == 3){
                retArr[i] = inputBytes[0];
                break;
            }
            retArr[i] = inputBytes[i+1];
        }
        
        // Put bytes through AES S-Box
        for (int i = 0; i < 4; i++){
            retArr[i] = keyScheduleSBoxFunction(retArr[i]);
        }

        // One of the bytes is XOR-ed with a round coefficient
        retArr[0] = keyScheduleRoundCoefficientFunction(retArr[0], round);

        return retArr;
    }

    /**
     * Puts a byte through the AES S-box function for key scheduling.
     * 
     * TODO: Implement S-box functionality. Just returns the original input for now.
     * @param inputByte Byte to be put through s-box
     * @return The new byte after AES S-box has been applied
     */
    private byte keyScheduleSBoxFunction(byte inputByte){
        return inputByte;
    }

    /**
     * Round coefficient function for key scheduling, used in the G Function on one byte.
     * 
     * @param inputByte Byte to be XOR-ed by a certain round coefficient
     * @param round Current round of key scheduling, necessary for correctly chosen round coefficient to XOR inputByte with
     * @return XOR-ed byte from inputByte and round coefficient
     */
    private byte keyScheduleRoundCoefficientFunction(byte inputByte, int round){
        // Select round coefficient based off of the round. See Key addition and key schedule in AESPDF.pdf
        byte xorByte = switch (round) {
            case 1 -> (byte)0b00000001;
            case 2 -> (byte)0b00000010;
            case 3 -> (byte)0b00000100;
            case 4 -> (byte)0b00001000;
            case 5 -> (byte)0b00010000;
            case 6 -> (byte)0b00100000;
            case 7 -> (byte)0b01000000;
            case 8 -> (byte)0b10000000;
            case 9 -> (byte)0b00011011;
            case 10 -> (byte)0b00110110;
            default -> (byte)0b00000000;
        };
        return (byte)(inputByte ^ xorByte);
    }

    /**
     * Returns KeyManager's entire roundKeys array
     * @return array of byte[] roundKeys
     */
    public byte[][] getRoundKeys(){
        return roundKeys;
    }

    /**
     * Returns a round key from KeyManager's roundKeys array given an index
     * @param index spot in array to retrieve round key
     * @return Correct byte[] round key from KeyManager's roundKeys array at index spot
     */
    public byte[] getRoundKey(int index){
        return roundKeys[index];
    }

    /**
     * Makes sure that the given key string for KeyManager constructor is the correct size,
     * which for 128 bit key would be 128 / 8 = 16
     * 
     * @param keyString String to check
     * @return The correct size of string, either padded with characters, cut, or the same
     */
    private String setCorrectStringKeySize(String keyString){
        if (keyString.length() > stringKeySize){
            keyString = keyString.substring(0, stringKeySize);
            System.err.println("Given key string size was longer than " + stringKeySize + ". Setting new key to: " + keyString);
        }else if (keyString.length() < stringKeySize){
            while (keyString.length() < stringKeySize){
                keyString = keyString + "0";
            }
            System.err.println("Given key string size was shorter than " + stringKeySize + ". Setting new key to: " + keyString);
        }
        return keyString;
    }

    /**
     * Processes a string into a byte array.
     * 
     * @param key key string to set KeyManager key variable to
     */
    private byte[] getByteKeyFromString(String key){
        byte[] newKey = key.getBytes(StandardCharsets.UTF_8);
        if (newKey.length != keySize/8){
            System.err.println("WARNING: Bad key size being used in KeyManager.java");
        }
        return newKey;
    }
}
