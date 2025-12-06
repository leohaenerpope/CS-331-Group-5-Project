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
    private static final byte[] SBOX = new byte[] {
        (byte)0x63,(byte)0x7c,(byte)0x77,(byte)0x7b,(byte)0xf2,(byte)0x6b,(byte)0x6f,(byte)0xc5,
        (byte)0x30,(byte)0x01,(byte)0x67,(byte)0x2b,(byte)0xfe,(byte)0xd7,(byte)0xab,(byte)0x76,
        (byte)0xca,(byte)0x82,(byte)0xc9,(byte)0x7d,(byte)0xfa,(byte)0x59,(byte)0x47,(byte)0xf0,
        (byte)0xad,(byte)0xd4,(byte)0xa2,(byte)0xaf,(byte)0x9c,(byte)0xa4,(byte)0x72,(byte)0xc0,
        (byte)0xb7,(byte)0xfd,(byte)0x93,(byte)0x26,(byte)0x36,(byte)0x3f,(byte)0xf7,(byte)0xcc,
        (byte)0x34,(byte)0xa5,(byte)0xe5,(byte)0xf1,(byte)0x71,(byte)0xd8,(byte)0x31,(byte)0x15,
        (byte)0x04,(byte)0xc7,(byte)0x23,(byte)0xc3,(byte)0x18,(byte)0x96,(byte)0x05,(byte)0x9a,
        (byte)0x07,(byte)0x12,(byte)0x80,(byte)0xe2,(byte)0xeb,(byte)0x27,(byte)0xb2,(byte)0x75,
        (byte)0x09,(byte)0x83,(byte)0x2c,(byte)0x1a,(byte)0x1b,(byte)0x6e,(byte)0x5a,(byte)0xa0,
        (byte)0x52,(byte)0x3b,(byte)0xd6,(byte)0xb3,(byte)0x29,(byte)0xe3,(byte)0x2f,(byte)0x84,
        (byte)0x53,(byte)0xd1,(byte)0x00,(byte)0xed,(byte)0x20,(byte)0xfc,(byte)0xb1,(byte)0x5b,
        (byte)0x6a,(byte)0xcb,(byte)0xbe,(byte)0x39,(byte)0x4a,(byte)0x4c,(byte)0x58,(byte)0xcf,
        (byte)0xd0,(byte)0xef,(byte)0xaa,(byte)0xfb,(byte)0x43,(byte)0x4d,(byte)0x33,(byte)0x85,
        (byte)0x45,(byte)0xf9,(byte)0x02,(byte)0x7f,(byte)0x50,(byte)0x3c,(byte)0x9f,(byte)0xa8,
        (byte)0x51,(byte)0xa3,(byte)0x40,(byte)0x8f,(byte)0x92,(byte)0x9d,(byte)0x38,(byte)0xf5,
        (byte)0xbc,(byte)0xb6,(byte)0xda,(byte)0x21,(byte)0x10,(byte)0xff,(byte)0xf3,(byte)0xd2,
        (byte)0xcd,(byte)0x0c,(byte)0x13,(byte)0xec,(byte)0x5f,(byte)0x97,(byte)0x44,(byte)0x17,
        (byte)0xc4,(byte)0xa7,(byte)0x7e,(byte)0x3d,(byte)0x64,(byte)0x5d,(byte)0x19,(byte)0x73,
        (byte)0x60,(byte)0x81,(byte)0x4f,(byte)0xdc,(byte)0x22,(byte)0x2a,(byte)0x90,(byte)0x88,
        (byte)0x46,(byte)0xee,(byte)0xb8,(byte)0x14,(byte)0xde,(byte)0x5e,(byte)0x0b,(byte)0xdb,
        (byte)0xe0,(byte)0x32,(byte)0x3a,(byte)0x0a,(byte)0x49,(byte)0x06,(byte)0x24,(byte)0x5c,
        (byte)0xc2,(byte)0xd3,(byte)0xac,(byte)0x62,(byte)0x91,(byte)0x95,(byte)0xe4,(byte)0x79,
        (byte)0xe7,(byte)0xc8,(byte)0x37,(byte)0x6d,(byte)0x8d,(byte)0xd5,(byte)0x4e,(byte)0xa9,
        (byte)0x6c,(byte)0x56,(byte)0xf4,(byte)0xea,(byte)0x65,(byte)0x7a,(byte)0xae,(byte)0x08,
        (byte)0xba,(byte)0x78,(byte)0x25,(byte)0x2e,(byte)0x1c,(byte)0xa6,(byte)0xb4,(byte)0xc6,
        (byte)0xe8,(byte)0xdd,(byte)0x74,(byte)0x1f,(byte)0x4b,(byte)0xbd,(byte)0x8b,(byte)0x8a,
        (byte)0x70,(byte)0x3e,(byte)0xb5,(byte)0x66,(byte)0x48,(byte)0x03,(byte)0xf6,(byte)0x0e,
        (byte)0x61,(byte)0x35,(byte)0x57,(byte)0xb9,(byte)0x86,(byte)0xc1,(byte)0x1d,(byte)0x9e,
        (byte)0xe1,(byte)0xf8,(byte)0x98,(byte)0x11,(byte)0x69,(byte)0xd9,(byte)0x8e,(byte)0x94,
        (byte)0x9b,(byte)0x1e,(byte)0x87,(byte)0xe9,(byte)0xce,(byte)0x55,(byte)0x28,(byte)0xdf,
        (byte)0x8c,(byte)0xa1,(byte)0x89,(byte)0x0d,(byte)0xbf,(byte)0xe6,(byte)0x42,(byte)0x68,
        (byte)0x41,(byte)0x99,(byte)0x2d,(byte)0x0f,(byte)0xb0,(byte)0x54,(byte)0xbb,(byte)0x16
    };
    

    /**
     * Constructor for making a KeyManager with a non-specified key string.
     * 
     * Uses a pre-determined key string of length 16. (16 chars = 16 * 8 bits per char = 128 bit size)
     * For Java and I guess in this project too, we are just doing 1 char = 8 bits
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
        retArr[0] = inputBytes[1];
        retArr[1] = inputBytes[2];
        retArr[2] = inputBytes[3];
        retArr[3] = inputBytes[0];
        
        // Put bytes through AES S-Box
        for (int i = 0; i < 4; i++){
            retArr[i] = keyScheduleSBoxFunction(retArr[i]);
        }

        // One of the bytes is XOR-ed with a round coefficient
        retArr[0] ^= keyScheduleRoundCoefficientFunction(round);

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
        int newByte = inputByte & 0xFF;
        return SBOX[newByte];
    }

    /**
     * Round coefficient function for key scheduling, used in the G Function on one byte.
     * 
     * @param inputByte Byte to be XOR-ed by a certain round coefficient
     * @param round Current round of key scheduling, necessary for correctly chosen round coefficient to XOR inputByte with
     * @return XOR-ed byte from inputByte and round coefficient
     */
    private byte keyScheduleRoundCoefficientFunction(int round){
        final byte[] rcon = {
            0x00,
            0x01,0x02,0x04,0x08,0x10,0x20,0x40,(byte)0x80,0x1B,0x36
        };
        return rcon[round];
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
     * Prints the key converted into bytes to the console
     */
    public void printKey(){
        System.out.print("\nKey in bytes: ");
        for(int i = 0; i < 16; i++){
            if(i != 15){
                System.out.print(key[i] + " ");
            }
            else{
                System.out.println(key[i]);
            }
        }
    }

    /**
     * Prints out each round key to the console
     */
    public void printRoundKeys(){
        System.out.println("\nRound keys:");
        for(int i = 1; i < 11; i++){
            System.out.print("Round key " + i + ": ");
            for(int j = 0; j < 16; j++){
                if(j != 15){
                    System.out.print(roundKeys[i][j] + " ");
                }
                else{
                    System.out.println(roundKeys[i][j]);
                }
            }
        }
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
