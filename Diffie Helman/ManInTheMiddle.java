import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ManInTheMiddle
{
    static void operate(Socket serverSocket, Socket clientSocket)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            // Streams for server connection
            DataInputStream serverIn = new DataInputStream(serverSocket.getInputStream());
            DataOutputStream serverOut = new DataOutputStream(serverSocket.getOutputStream());
            
            // Streams for client connection
            DataInputStream clientIn = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientOut = new DataOutputStream(clientSocket.getOutputStream());
            
            System.out.println("\n=== MAN-IN-THE-MIDDLE ATTACK ===\n");
            
            // Receive p and g from server
            String params = serverIn.readUTF();
            String[] P = params.split("\\s+");
            int p = Integer.parseInt(P[0]);
            int g = Integer.parseInt(P[1]);
            
            System.out.println("Intercepted from Server: p = " + p + ", g = " + g);
            
            // Forward p and g to client
            clientOut.writeUTF(params);
            System.out.println("Forwarded p and g to Client");
            
            // Create two DiffieHellman instances - one for server, one for client
            DiffieHellman dhForServer = new DiffieHellman(p, g);
            DiffieHellman dhForClient = new DiffieHellman(p, g);
            
            // Get MITM's private keys
            System.out.print("\nEnter MITM's private key for Server connection: ");
            int mitmPrivateKeyServer = Integer.parseInt(br.readLine());
            
            System.out.print("Enter MITM's private key for Client connection: ");
            int mitmPrivateKeyClient = Integer.parseInt(br.readLine());
            
            // Generate MITM's public keys
            int mitmPublicKeyForServer = dhForServer.generatePublicKey(mitmPrivateKeyServer);
            int mitmPublicKeyForClient = dhForClient.generatePublicKey(mitmPrivateKeyClient);
            
            System.out.println("\nMITM's Public Key (for Server): " + mitmPublicKeyForServer);
            System.out.println("MITM's Public Key (for Client): " + mitmPublicKeyForClient);
            
            // Receive server's public key
            int serverPublicKey = Integer.parseInt(serverIn.readUTF());
            System.out.println("\nIntercepted Server's Public Key: " + serverPublicKey);
            
            // Send MITM's public key to server (pretending to be client)
            serverOut.writeUTF(Integer.toString(mitmPublicKeyForServer));
            System.out.println("Sent fake Client Public Key to Server: " + mitmPublicKeyForServer);
            
            // Send MITM's public key to client (pretending to be server)
            clientOut.writeUTF(Integer.toString(mitmPublicKeyForClient));
            System.out.println("Sent fake Server Public Key to Client: " + mitmPublicKeyForClient);
            
            // Receive client's public key
            int clientPublicKey = Integer.parseInt(clientIn.readUTF());
            System.out.println("Intercepted Client's Public Key: " + clientPublicKey);
            
            // Compute shared secrets
            int sharedSecretWithServer = dhForServer.computeSharedSecret(serverPublicKey);
            int sharedSecretWithClient = dhForClient.computeSharedSecret(clientPublicKey);
            
            System.out.println("\n=== ATTACK SUCCESSFUL ===");
            System.out.println("Shared Secret with Server: " + sharedSecretWithServer);
            System.out.println("Shared Secret with Client: " + sharedSecretWithClient);
            System.out.println("=========================");
            System.out.println("\nMITM can now decrypt all messages from both parties!");
            System.out.println("Server thinks it's talking to Client (shared key: " + sharedSecretWithServer + ")");
            System.out.println("Client thinks it's talking to Server (shared key: " + sharedSecretWithClient + ")");
            System.out.println("\nNote: Server and Client have DIFFERENT keys and don't know about the attack!");
            
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
            System.out.println("=== Man-In-The-Middle Attack Simulator ===");
            System.out.println("Starting MITM proxy...\n");
            
            // Connect to the server (acting as client)
            Socket serverSocket = new Socket("localhost", 8000);
            System.out.println("Connected to Server on port 8000");
            
            // Create server socket for client to connect (acting as server)
            ServerSocket mitmServerSocket = new ServerSocket(8001);
            System.out.println("Listening for Client on port 8001");
            System.out.println("Note: Client should connect to port 8001 instead of 8000\n");
            
            Socket clientSocket = mitmServerSocket.accept();
            System.out.println("Client connected to MITM proxy\n");
            
            operate(serverSocket, clientSocket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
