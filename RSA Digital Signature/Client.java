import java.io.*;
import java.net.Socket;

public class Client {

    static void operate(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            RSASignature rsa = new RSASignature(2048);

            String publicKey = rsa.getPublicKey();
            String privateKey = rsa.getPrivateKey();

            System.out.println("RSA Keys Generated:");
            System.out.println("Public Key  (n e): " + publicKey);
            System.out.println("Private Key (n d): " + privateKey);

            while (true) {
                System.out.print("\nEnter Message: ");
                String message = br.readLine();
                String signature = rsa.sign(message);
                String payload = message + "###" + signature + "###" + publicKey;

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
            Socket socket = new Socket("127.0.0.1", 9001);
            System.out.println("Connected to Server");
            operate(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
