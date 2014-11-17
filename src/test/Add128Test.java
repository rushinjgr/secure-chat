import java.security.SecureRandom;

import static org.junit.Assert.*;

public class Add128Test {

    Add128 no_args;
    SecureRandom random;
    byte bytes[];
    Add128 key_supplied;

    @org.junit.Before
    public void setUp() throws Exception {
        no_args = new Add128();
        random = new SecureRandom();
        bytes = new byte[128];
        key_supplied = new Add128(bytes);
    }

    @org.junit.Test
    public void testGetKey() throws Exception {
        assertEquals(key_supplied.getKey(),bytes);
    }

    @org.junit.Test
    public void testEncodeDecodeNoArgs() throws Exception {
        testEncodeDecode(no_args);
    }

    @org.junit.Test
    public void testEncodeDecodeSupplied() throws Exception {
        testEncodeDecode(key_supplied);
    }

    public void testEncodeDecode(Add128 enc) throws Exception {
        String message = "Hello world!";
        byte[] encrypted = enc.encode(message);
        String decrypted = enc.decode(encrypted);
        assertEquals(message,decrypted);
    }

    @org.junit.Test
    public void sanityCheck() throws Exception{
        String message = "Hello world!";
        byte b[] = message.getBytes();
        String back = new String (b);
        assertEquals(message, back);
    }
}