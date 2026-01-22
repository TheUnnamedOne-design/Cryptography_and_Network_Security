import java.io.BufferedReader;
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
            IDEAEncryption idea = new IDEAEncryption();

            while (true) { 
                System.out.print("Enter plain text: ");
                String plaintext = br.readLine();
                if (plaintext == null || plaintext.equalsIgnoreCase("exit")) break;
                
                System.out.print("Enter key: ");
                String key = br.readLine();
                
                String encrypted = idea.encrypt(plaintext, key);
                byte[] cipherBytes;
                try {
                    cipherBytes = encrypted.getBytes("ISO-8859-1");
                } catch (Exception e) {
                    cipherBytes = encrypted.getBytes();
                }
                
                out.writeInt(cipherBytes.length);
                out.write(cipherBytes);
                out.flush();
                
                System.out.println("Ciphertext sent (Hex): " + bytesToHex(encrypted));

                String decryptedResponse = in.readUTF();
                System.out.println("Server (Decrypted Output): " + decryptedResponse);
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
            Socket socket=new Socket("127.0.0.1",pn);
            System.out.println("Device connected to server");

            operate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}