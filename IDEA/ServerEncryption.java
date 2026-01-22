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
            IDEAEncryption idea = new IDEAEncryption();

            while (true) {
                int length = in.readInt();
                if (length <= 0) break;
                
                byte[] cipherBytes = new byte[length];
                in.readFully(cipherBytes);
                
                String ciphertext;
                try {
                    ciphertext = new String(cipherBytes, "ISO-8859-1");
                } catch (Exception e) {
                    ciphertext = new String(cipherBytes);
                }
                
                System.out.println("Received Ciphertext (Hex): " + bytesToHex(ciphertext));
                
                System.out.print("Enter key to decrypt: ");
                String key = br.readLine();
                
                String decrypted = idea.decrypt(ciphertext, key);
                System.out.println("Decrypted Text: " + decrypted);

                out.writeUTF(decrypted);
                out.flush();
            }
        }
        catch(Exception e)
        {
            System.out.println("Connection closed.");
        }
    }

    private static String bytesToHex(String s) {
        byte[] b; try { b = s.getBytes("ISO-8859-1"); } catch (Exception e) { b = s.getBytes(); }
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02X", x));
        return sb.toString();
    }
    public static void main(String argsp[])
    {
        try
        {
            int pn=9000;
            ServerSocket server=new ServerSocket(pn);
            System.out.println("Server listening on port "+pn);
            Socket socket=server.accept();
            System.out.println("Device connected");

            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}