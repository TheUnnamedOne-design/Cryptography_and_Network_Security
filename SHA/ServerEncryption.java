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
            
            SHA512 sha512 = new SHA512();
            
            while (true) {
                String message = in.readUTF();
                
                if(message.equals("EXIT")) {
                    System.out.println("Client disconnected");
                    break;
                }
                
                String receivedHash = in.readUTF();
                
                System.out.println("Received Message: " + message);
                System.out.println("Received SHA-512 Hash: " + receivedHash);
                
                // Compute SHA-512 hash on server side
                String computedHash = sha512.hash(message);
                System.out.println("Computed SHA-512 Hash: " + computedHash);
                
                String response;
                if(receivedHash.equals(computedHash)) {
                    response = "SUCCESS: Message is tamper-free. SHA-512 Hashes match!";
                    System.out.println("Verification Successful - Message is tamper-free");
                } else {
                    response = "FAILURE: Message has been tampered. SHA-512 Hashes do not match!";
                    System.out.println("Verification Failed - Message has been tampered");
                }
                
                out.writeUTF(response);
                System.out.println();
                
                // Check if client wants to exit
                try {
                    String nextMessage = in.readUTF();
                    if (nextMessage.equals("EXIT")) {
                        System.out.println("Client disconnected");
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
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
            int pn = 9000;
            ServerSocket server = new ServerSocket(pn);
            System.out.println("SHA-512 Server listening on port " + pn);
            Socket socket = server.accept();
            System.out.println("Device connected");
            System.out.println();

            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
