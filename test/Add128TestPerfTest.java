import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class Add128TestPerfTest {

    //677 bytes
    final String ipsum = new String("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ultricies sem lorem, quis molestie ligula suscipit nec. Nulla commodo luctus leo, ut lobortis justo ullamcorper sed. Fusce commodo libero id lorem varius, in dignissim enim vestibulum. Duis consectetur sem ac ipsum feugiat lobortis. Donec sollicitudin, nibh eget porta laoreet, lectus erat mattis nisl, id dictum nulla leo id purus. Nunc odio ex, feugiat eu dignissim in, finibus id neque. Nunc in elementum mauris. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus id euismod magna. Donec commodo aliquam ex, id luctus arcu posuere et. Nullam at est molestie, efficitur lacus eu, hendrerit velit.");

        //gets the average time to encrypt a message for the provided sample size (of keys), number of desired keys to test on and keylength
    //mean metric that is returned is returned as an averaging of the average time of each key
    public long encrypt_test_time(int keylength, int sample, int iterations){
        long results = 0;
        for (int i = 0; i < sample; i++) {
            long keyavg = 0;
            long time = 0;
            Add128 cipher = new Add128(keylength);
            for (int j = 0; j < iterations; j++) {
                time = System.nanoTime();
                cipher.encode(ipsum);
                keyavg += System.nanoTime() - time;
            }
            results += keyavg / iterations;
            System.out.println(keyavg / iterations);
        }
        results = results / sample;
        return results;
    }

    //finds the average time to make a key of x length based on sample size
    public long construct_test_time(int keylength, int sample){
        System.out.println(keylength + " l");
        long results = 0;
        for (int i = 0; i < sample; i++) {
            long time = 0;
            time = System.nanoTime();
            Add128 cipher = new Add128(keylength);
            results += System.nanoTime() - time;
        }
        results = results / sample;
        return results;
    }

    //gets the average time to decrypt a message for the provided sample size and keylength
    public long decrypt_test_time(int keylength, int sample, int iterations){
        long results = 0;
        for (int i = 0; i < sample; i++) {
            long keyavg = 0;
            long time = 0;
            Add128 cipher = new Add128(keylength);
            byte[] target = cipher.encode(ipsum);
            for (int j = 0; j < iterations; j++) {
                time = System.nanoTime();
                cipher.decode(target);
                keyavg += System.nanoTime() - time;
            }
            results += keyavg / iterations;
            System.out.println(keyavg / iterations);
        }
        results = results / sample;
        return results;
    }

    @Test
    public void driver() throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd--HH-mm--ss");
        Date date = new Date();
        FileWriter out = new FileWriter(dateFormat.format(date) + "additivePerfLog.txt");
        for (int i = 2; i < 24; i++) {
            int factor = (int) Math.pow(2,i);
            out.write("time to construct cipher of key length 2^" + i + " in nanos: " + construct_test_time(factor, 50)+"\n");
        }
        for (int i = 2; i < 24; i++) {
            int factor = (int) Math.pow(2,i);
            out.write("time to encrypt with cipher of key length 2^" + i + " in nanos: " + encrypt_test_time(factor, 10,10)+"\n");
        }
        for (int i = 2; i < 24; i++) {
            int factor = (int) Math.pow(2,i);
            out.write("time to decrypt with cipher of key length 2^" + i + " in nanos: " + encrypt_test_time(factor, 10,10)+"\n");
        }
        out.close();
    }

}