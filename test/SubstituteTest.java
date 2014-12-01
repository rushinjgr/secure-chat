import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.*;

public class SubstituteTest {

    Substitute no_args;
    SecureRandom random;
    byte bytes[];
    Substitute key_supplied;

    @Before
    public void setUp() throws Exception {
        no_args = new Substitute();
        random = new SecureRandom();
        bytes = new byte[256];
        key_supplied = new Substitute(bytes);
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals(key_supplied.getKey(),bytes);
    }

    @Test
    public void testEncodeDecodeNoArgs() throws Exception {
        testEncodeDecode(no_args);
    }

    @Test
    public void testEncodeDecodeNoArgs2() throws Exception {
        testEncodeDecode2(no_args);
    }

    public void testEncodeDecodeSupplied() throws Exception {
        testEncodeDecode(key_supplied);
    }

    public void testEncodeDecode(Substitute enc) throws Exception{
        String message = "Hello world!";
        byte[] encrypted = enc.encode(message);
        String decrypted = enc.decode(encrypted);
        assertEquals(message,decrypted);
    }

    public void testEncodeDecode2(Substitute enc) throws Exception{
        String message = "In 1846, on March, the 18th day\n" +
                         "We hoisted our colors to the top of the mast\n" +
                         "And for Greenland sailed away";
        byte[] encrypted = enc.encode(message);
        String decrypted = enc.decode(encrypted);
        assertEquals(message,decrypted);
    }
}