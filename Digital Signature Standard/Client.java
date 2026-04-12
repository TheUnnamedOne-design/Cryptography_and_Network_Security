import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class Client {

    static void operate(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            DSS_DSA dsa = new DSS_DSA(23, 11, 4);

            // Generate keys
            BigInteger x = dsa.generatePrivateKey();
            BigInteger y = dsa.generatePublicKey(x);

            String yHex = DSS_DSA.bigIntegerToHex(y);

            System.out.println("Private Key (x): " + x);
            System.out.println("Public Key (y): " + yHex);

            while (true) {
                System.out.print("\nEnter Message: ");
                String message = br.readLine();

                String signature = dsa.sign(message, x);

                // Send: message###signature###y
                String payload = message + "###" + signature + "###" + yHex;

                out.writeUTF(payload);

                System.out.println("Sent:");
                System.out.println("Message   : " + message);
                System.out.println("Signature : " + signature);

                String response = in.readUTF();
                System.out.println("Server: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9000);
            System.out.println("Connected to Server");
            operate(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}