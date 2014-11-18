import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    //changed port according to specs
    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;

    BigInteger E;
    BigInteger N;
    String Pref;
    SymCipher ciph;
    BigInteger ciph_key;

    public SecureChatClient ()
    {
        try {
            System.out.println("Starting secure Chat!");
            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr =
                    InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new
            // Socket


            //it creates an ObjectOutputStream on the stocket for writing
            myWriter = new ObjectOutputStream(connection.getOutputStream());
            //and immediately calls the .flush() method to prevent deadlock
            myWriter.flush();
            //it creates an ObjectInputStream on the socket
            myReader = new ObjectInputStream(connection.getInputStream());
            //it receives the server's public key, e , as a BigInteger Object
            E = (BigInteger) myReader.readObject();
            //it receives the server's public mod value, n, as a BigInteger Object
            N = (BigInteger) myReader.readObject();
            //it receives the server's preferred symmetric cipher
            //either "Sub" or "Add"
            //as a String object
            Pref = (String) myReader.readObject();

            //then outputs the type of symmetric encryption
            //Add128 or Substitute
            //to the console
            //based on cipher preference, it creates either a Substitute or Add128
            //storing the resulting object in a SympCipher Variable
            if(Pref.compareToIgnoreCase("Sub")==0){
                //substitution
                System.out.println("Substitution Encryption enabled.");
                ciph = new Substitute();
            }else{
                //add 128
                System.out.println("Add128 Encryption enabled.");
                ciph = new Add128();
            }
            //it gets the key from the cipher object using the getKey method
            //converts result into a BigInteger object
            //ensure BigInteger is positive
            //uses BigInteger constructor that takes a sign magnitude representation
            //of a Biginteger
            ciph_key = new BigInteger(1,ciph.getKey());
            //then outputs a biginteger representation of the symmetric key to console
            System.out.println("Key: " + ciph_key);
            //RSA encrypts BigInteger version of the key using e and n
            //sends resulting BigInteger to the server
            myWriter.writeObject(ciph_key.modPow(E,N));
            myWriter.flush();

            //encrypt name with cipher
            //send it to the server
            //encryption will be done using encode() method of SymCipher
            //resulting array of bytes sent to server as a single object
            //using the ObjectOutputStream
            myWriter.writeObject(ciph.encode(myName));
            this.setTitle(myName);	  // Set title to identify chatter

            Box b = Box.createHorizontalBox();  // Set up graphical environment for
            outputArea = new JTextArea(8, 30);  // user
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type input
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings
            outputThread.start();					// from Server

            addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            try {
                                myWriter.writeObject(new String("CLIENT CLOSING"));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            System.exit(0);
                        }
                    }
            );

            setSize(500, 200);
            setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Oh noes");
            e.printStackTrace(System.err);
            //System.out.println("Problem starting client!");
        }
    }

    public void run()
    {
        while (true)
        {
            try {
                String currMsg = ciph.decode(myReader.readLine().getBytes());
                outputArea.append(currMsg+"\n");
            }
            catch (Exception e)
            {
                System.out.println(e +  ", closing client!");
                break;
            }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String currMsg = e.getActionCommand();	  // Get input value
        inputField.setText("");
        try {
            myWriter.writeObject(ciph.encode(myName + ":" + currMsg));   // Add name and send it
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }											 	// to Server

    public static void main(String [] args)
    {
        ChatClient JR = new ChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}