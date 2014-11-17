import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class Substitute implements SymCipher{

    byte[] key;
    byte[] dkey;


    public Substitute(){
        key = keyArrayGen();
        dkey = dkeyGen(key);
    }

    public Substitute(byte[] bytekey){
        if (bytekey.length != 256){
            key = null;
            System.err.println("invalid key length supplied");
        }
        else {
            key = bytekey;
            dkey = dkeyGen(key);
        }
    }

    public byte[] keyArrayGen(){
        ArrayList<Integer> vals = new ArrayList<Integer>();
        for(int i = 0; i < 256; i++){
            vals.add(i);
        }
        Collections.shuffle(vals);
        byte[] g = new byte[256];
        for(int i = 0; i < 256; i++){
            g[i] = (byte)((int) vals.get(i));
        }
        return g;
    }

    public byte[] dkeyGen(byte[] k){
        byte[] d = new byte[k.length];
        for (int i = 0; i < 256; i++) {
            if(k[i] < 0){
                d[k[i]+256] = (byte) (i);
            }else {
                d[k[i]] = (byte) (i);
            }
        }
        return d;
    }

    @Override
    public byte[] getKey() {
        return key;
    }

    @Override
    public byte[] encode(String S) {
        byte[] result = S.getBytes();
        int i = 0;
        int j = 0;
        //if shorter than key, ignore remaining bytes in key
        //if longer, cycle through the key until the end
        while(result.length < i){
            if(j == key.length){
                j = 0;
            }
            result[i] = (key[result[i]]);
            j++;
            i++;
        }
        return result;
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
            bytes[i] = (byte) (dkey[bytes[i]]);
            j++;
            i++;
        }
        //convert the resulting byte array back to a string and return it
        return new String(bytes);
    }
}
