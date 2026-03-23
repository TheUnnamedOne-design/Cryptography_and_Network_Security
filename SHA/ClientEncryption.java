import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.Socket;

public class ClientEncryption
{
    static void operate(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            SHA512 sha512 = new SHA512();
            
            // Read message from message.txt
            StringBuilder content = new StringBuilder();
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader("message.txt"));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }
                fileReader.close();
                
                if(content.length() > 0 && content.charAt(content.length()-1) == '\n') {
                    content.deleteCharAt(content.length()-1);
                }
                
            } catch(Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
            
            String message = content.toString();
            
            // Compute SHA-512 hash
            String hash = sha512.hash(message);
            System.out.println("Message from file: " + message);
            System.out.println("Computed SHA-512 Hash: " + hash);
            System.out.println();
            
            // Send message and hash to server
            out.writeUTF(message);
            out.writeUTF(hash);
            System.out.println("Message and hash sent to server");
            
            // Receive server response
            String result = in.readUTF();
            System.out.println("Server Response: " + result);
            System.out.println();
            
            // Close connection
            out.writeUTF("EXIT");
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
            Socket socket = new Socket("127.0.0.1", pn);
            System.out.println("Device connected to server");
            System.out.println();

            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
