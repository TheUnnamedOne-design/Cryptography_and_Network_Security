import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static void operate(Socket socket) {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            DSS_DSA dsa = new DSS_DSA(23, 11, 4);

            while (true) {

                String received = in.readUTF();

                // Format: message###signature###y
                String[] packet = received.split("###");

                String message = packet[0];
                String signature = packet[1];
                String yHex = packet[2];

                System.out.println("\nReceived:");
                System.out.println("Message   : " + message);
                System.out.println("Signature : " + signature);
                System.out.println("Public Key: " + yHex);

                // Verify original message
                boolean validOriginal = dsa.verify(signature, message, yHex);

                // Modify message (tampering)
                String modifiedMessage = "Modified_" + message;

                boolean validModified = dsa.verify(signature, modifiedMessage, yHex);

                String result = "Original Valid: " + validOriginal +
                                " | Modified Valid: " + validModified;

                out.writeUTF(result);

                System.out.println("Verification:");
                System.out.println("Original  : " + validOriginal);
                System.out.println("Modified  : " + validModified);
            }

        } catch (Exception e) {ṭ
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(9000);
            System.out.println("Server listening on port 9000");

            Socket socket = server.accept();
            System.out.println("Client connected");

            operate(socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}