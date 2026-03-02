import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientEncryption
{
    static void operate(Socket socket)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            // Receive prime p and generator g from server
            String params = in.readUTF();
            String[] P = params.split("\\s+");
            
            int p = Integer.parseInt(P[0]);
            int g = Integer.parseInt(P[1]);
            
            System.out.println("Received p = " + p + ", g = " + g);
            
            // Create DiffieHellman instance
            DiffieHellman dh = new DiffieHellman(p, g);
            
            // Get client's private key
            System.out.print("Enter your private key: ");
            int clientPrivateKey = Integer.parseInt(br.readLine());
            
            // Generate client's public key
            int clientPublicKey = dh.generatePublicKey(clientPrivateKey);
            System.out.println("Client's Public Key: " + clientPublicKey);
            
            // Receive server's public key
            int serverPublicKey = Integer.parseInt(in.readUTF());
            System.out.println("Received Server's Public Key: " + serverPublicKey);
            
            // Send client's public key to server
            out.writeUTF(Integer.toString(clientPublicKey));
            System.out.println("Sent Client's Public Key to Server");
            
            // Compute shared secret
            int sharedSecret = dh.computeSharedSecret(serverPublicKey);
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
            Socket socket = new Socket("localhost", 8000);
            System.out.println("Connected to Server");
            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
