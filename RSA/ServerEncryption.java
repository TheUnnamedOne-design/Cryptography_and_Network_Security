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
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in=new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            String s="";
            
            try
            {
                while (true) {
                    
                    int message=Integer.parseInt(in.readUTF());
                    System.out.println("Received cipher message : "+message);
                    System.out.print("Enter n: ");
                    int n=Integer.parseInt(br.readLine());
                    //System.out.print("Enter cipher: ");
                    System.out.print("Enter d : ");
                    int d=Integer.parseInt(br.readLine());
                    RSA rsa=new RSA(n,d,true);
                    s=Integer.valueOf(rsa.decrypt(message)).toString();
                    out.writeUTF(s);
                    System.out.println("Server Decrypted : "+s);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
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