import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class Add128TestSecTest {

    //largest factor of 2 to try to brute force
    static final int maxKeyFactor = 2;
    //number of trials per key length
    //each will have a separate key but the same message
    static final int trials = 5;
    //677 bytes
    final String ipsum = new String("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ultricies sem lorem, quis molestie ligula suscipit nec. Nulla commodo luctus leo, ut lobortis justo ullamcorper sed. Fusce commodo libero id lorem varius, in dignissim enim vestibulum. Duis consectetur sem ac ipsum feugiat lobortis. Donec sollicitudin, nibh eget porta laoreet, lectus erat mattis nisl, id dictum nulla leo id purus. Nunc odio ex, feugiat eu dignissim in, finibus id neque. Nunc in elementum mauris. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus id euismod magna. Donec commodo aliquam ex, id luctus arcu posuere et. Nullam at est molestie, efficitur lacus eu, hendrerit velit.");
    //we will only brute force factors of 2
    byte[][][] targets = new byte[maxKeyFactor][trials][];

    @Before
    public void setUp() throws Exception {
        for (int i = 1; i <= maxKeyFactor; i++) {
            for (int j = 0; j < trials; j++) {
                Add128 ciph = new Add128((int)Math.pow(2,i));
                targets[i-1][j] = ciph.encode(ipsum);
            }
        }
    }


    @Test
    public void bruteforceTest() throws Exception{
        for (int i = 1; i <= maxKeyFactor; i++) {
            for (int j = 0; j < trials; j++) {
                assertEquals(bruteForceTimed(targets[i - 1][j]),true);
            }
        }
    }

    private boolean bruteForceTimed(byte[] bytes) {
        long time = System.currentTimeMillis();
        byte[] guess = new byte[bytes.length];
        while(new String(guess).compareToIgnoreCase(ipsum) != 0){
            return(bruteForceTimed(bytes,guess));
        }
        System.out.println("Time Elapsed" + (System.currentTimeMillis() - time));
        return true;
    }

    private boolean bruteForceTimed(byte[] bytes, byte[] guess) {
        //todo implement
        return false;
    }
}