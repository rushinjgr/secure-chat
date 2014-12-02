import org.junit.Test;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SecureChatClientPerfTest {

    //wrapper for client constructor
    public SecureChatClient generateClient(int c,String server) throws InterruptedException {
        SecureChatClient cli = new SecureChatClient(Integer.toString(c),server);
        Thread.sleep(1500); //time to connect
        return cli;
    }

    //sends a string argument as the message from a client to its server
    public void sendMsg(SecureChatClient cli, String msg) throws Exception {
        cli.inputField.setText(msg);
        cli.inputField.postActionEvent();
    }

    //returns as long the number of nanoseconds from
    //time of message sent from client (after encryption)
    //to the time the message was received by the server, decrypted, re-encrypted, and sent back to the client
    //before decryption by client
    //NOTE: if multiple clients are sending messages to the server at this time, interferance can occur
    //in this situation this test is not reliable
    public long sendMsg_timed(SecureChatClient cli, String msg) throws Exception{
        int temp = cli.messagesReceived;
        long startTime = System.nanoTime();
        sendMsg(cli,"hello");
        while(cli.messagesReceived == temp){
            Thread.sleep(1);
        }
        long elapsedTime = System.nanoTime() - startTime;
        return elapsedTime;
    }

    //accepts the url of the server as string
    public long responseTime(SecureChatClient cli) throws Exception{
        return sendMsg_timed(cli,"helloo");
    }

    //compare the response time b/w localhost and local network server
    //perform specified # of trials and log the output
    public void responseTimeIter(int sample) throws Exception {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd--HH-mm--ss");
            Date date = new Date();
            FileWriter out = new FileWriter(dateFormat.format(date) + "resptimeLog.txt");
            long networkTimes[] = new long[sample];
            long networkTime = 0;
            long localhostTimes[] = new long[sample];
            long localhostTime = 0;
            SecureChatClient net = generateClient(1, "10.0.0.6");
            SecureChatClient loc = generateClient(1, "localhost");
            for (int i = 0; i < sample; i++) {
                long time = responseTime(net);
                networkTimes[i] = time;
                networkTime += time;
                out.write("network: " + TimeUnit.NANOSECONDS.toMillis(time) + "\n");
                Thread.sleep(1000);
                time = responseTime(loc);
                localhostTimes[i] = time;
                localhostTime += time;
                out.write("local: " + TimeUnit.NANOSECONDS.toMillis(time) + "\n");
                Thread.sleep(1000);
            }
            networkTime = networkTime / sample;
            localhostTime = localhostTime / sample;
            out.write("network average (nano): " + networkTime + "\n");
            out.write("localhost average (nano): " + localhostTime + "\n");
            out.close();
    }

    @Test
    //default max users is 30
    //try with 25 users connected to each server
    //yay load testing
    public void responseTime50_load20() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd--HH-mm--ss");
        Date date = new Date();
        System.out.println("load test beginning at " + dateFormat.format(date));
        for (int i = 0; i < 25; i++) {
            SecureChatClient net = generateClient(1, "10.0.0.6");
        }

        for (int i = 0; i < 25; i++) {
            SecureChatClient net = generateClient(1, "localhost");
        }
        date = new Date();
        System.out.println("load clients connected at " + dateFormat.format(date));
        responseTimeIter(50);
        date = new Date();
        System.out.println("load test ending at " + dateFormat.format(date));
    }

    @Test
    //default max users is 30
    //try with 25 users connected to each server
    public void responseTime50_wrapper() throws Exception {
        responseTimeIter(50);
    }
}