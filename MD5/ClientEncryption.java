import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientEncryption
{
    static void operate(Socket socket)
    {
        try
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in=new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            
            MD5 md5 = new MD5();
            
            while (true) {
                System.out.print("Enter filename (or 'exit' to quit): ");
                String filename = br.readLine();
                
                if(filename.equalsIgnoreCase("exit")) {
                    out.writeUTF("EXIT");
                    break;
                }
                
                StringBuilder content = new StringBuilder();
                try {
                    BufferedReader fileReader = new BufferedReader(new FileReader(filename));
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
                    continue;
                }
                
                String message = content.toString();
                
                String hash = md5.hash(message);
                System.out.println("Computed Hash: " + hash);
                
                out.writeUTF(message);
                out.writeUTF(hash);
                System.out.println("Message and hash sent to server");
                
                String result = in.readUTF();
                System.out.println("Server Response: " + result);
                System.out.println();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String argsp[])
    {
        try
        {
            int pn=9000;
            Socket socket=new Socket("127.0.0.1",pn);
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
