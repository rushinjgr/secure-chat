import java.security.SecureRandom;

public class Add128 implements SymCipher{

    byte[] key;

    //create random 128 byte additive key
    //store in an array of bytes
    public Add128(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[128];
        random.nextBytes(bytes);
        key = bytes;
    }

    //use byte array as key
    public Add128(byte[] bytekey){
        if (bytekey.length != 128){
            key = null;
            System.err.println("invalid key length supplied");
            System.err.println("supplied key is of length: " + bytekey.length);
        }
        else {
            key = bytekey;
        }
    }

    @Override
    public byte[] getKey() {
        return key;
    }

    @Override
    public byte[] encode(String S) {
        if(key == null){
            return null;
        }
        //convert string parameter to byte array
        byte[] message = S.getBytes();
        //add the corresponding byte of the key to each index in the array of bytes
        int i = 0;
        int j = 0;
        //if shorter than key, ignore remaining bytes in key
        //if longer, cycle through the key until the end
        while(message.length < i){
            if(j == key.length){
                j = 0;
            }
            message[i] = (byte) (message[i] + key[j]);
            j++;
            i++;
        }
        //return encrypted array of bytes
        return message;
    }

    @Override
    public String decode(byte[] bytes) {
        //subtract the corresponding byte of the key from each index of the array of bytes
        //if the message is shorter than the key, ignore remaining key bytes
        //if the message is longer, cycle through as many times as needed
        if(key == null){
            return null;
        }
        //convert string parameter to byte array
        //add the corresponding byte of the key to each index in the array of bytes
        int i = 0;
        int j = 0;
        //if shorter than key, ignore remaining bytes in key
        //if longer, cycle through the key until the end
        while(bytes.length < i){
            if(j == key.length){
                j = 0;
            }
            bytes[i] = (byte) (bytes[i] - key[j]);
            j++;
            i++;
        }
        //convert the resulting byte array back to a string and return it
        return new String(bytes);
    }
}