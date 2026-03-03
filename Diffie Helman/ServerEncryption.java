import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEncryption
{
    static void operate(Socket socket)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            // Get prime number p and generator g
            int p = 0;
            while (true) {
                try {
                    System.out.print("Enter prime number (p): ");
                    String input = br.readLine();
                    if (input != null && !input.trim().isEmpty()) {
                        p = Integer.parseInt(input.trim());
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
            
            int g = 0;
            while (true) {
                try {
                    System.out.print("Enter generator (g): ");
                    String input = br.readLine();
                    if (input != null && !input.trim().isEmpty()) {
                        g = Integer.parseInt(input.trim());
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
            
            // Send p and g to client
            out.writeUTF(p + " " + g);
            System.out.println("Sent p and g to Client");
            
            // Create DiffieHellman instance
            DiffieHellman dh = new DiffieHellman(p, g);
            
            // Get server's private key
            int serverPrivateKey = 0;
            while (true) {
                try {
                    System.out.print("Enter your private key: ");
                    String input = br.readLine();
                    if (input != null && !input.trim().isEmpty()) {
                        serverPrivateKey = Integer.parseInt(input.trim());
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
            
            // Generate server's public key
            int serverPublicKey = dh.generatePublicKey(serverPrivateKey);
            System.out.println("Server's Public Key: " + serverPublicKey);
            
            // Send server's public key to client
            out.writeUTF(Integer.toString(serverPublicKey));
            System.out.println("Sent Server's Public Key to Client");
            
            // Receive client's public key
            int clientPublicKey = Integer.parseInt(in.readUTF());
            System.out.println("Received Client's Public Key: " + clientPublicKey);
            
            // Compute shared secret
            int sharedSecret = dh.computeSharedSecret(clientPublicKey);
            System.out.println("\n=== KEY EXCHANGE COMPLETE ===");
            System.out.println("Shared Secret Key: " + sharedSecret);
            System.out.println("=============================\n");
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server started on port 8000");
            System.out.println("Waiting for client...");
            
            Socket socket = serverSocket.accept();
            System.out.println("Client connected\n");
            
            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
