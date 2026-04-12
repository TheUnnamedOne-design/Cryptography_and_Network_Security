import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static void operate(Socket socket) {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            RSASignature rsa = new RSASignature(2048);

            while (true) {

                String received = in.readUTF();
                String[] packet = received.split("###");

                String message = packet[0];
                String signature = packet[1];
                String publicKey = packet[2];

                System.out.println("\nReceived:");
                System.out.println("Message   : " + message);
                System.out.println("Signature : " + signature);
                System.out.println("Public Key: " + publicKey);
                boolean validOriginal = rsa.verify(signature, message, publicKey);
                String modifiedMessage = "Modified_" + message;

                boolean validModified = rsa.verify(signature, modifiedMessage, publicKey);

                String result = "Original Valid: " + validOriginal +
                                " | Modified Valid: " + validModified;

                out.writeUTF(result);

                System.out.println("Verification:");
                System.out.println("Original  : " + validOriginal);
                System.out.println("Modified  : " + validModified);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(9001);
            System.out.println("Server listening on port 9001");

            Socket socket = server.accept();
            System.out.println("Client connected");

            operate(socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
