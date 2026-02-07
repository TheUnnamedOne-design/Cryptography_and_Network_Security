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
            
            
            System.out.print("Enter n: ");
            int n = Integer.parseInt(br.readLine());

            System.out.print("Enter e: ");
            int e = Integer.parseInt(br.readLine());

            System.out.print("Enter d: ");
            int d = Integer.parseInt(br.readLine());

            
            out.writeUTF(n + " " + e);

            RSA rsa = new RSA(n, d, true);

           
            while (true) {
                int cipher = Integer.parseInt(in.readUTF());
                System.out.println("Client sent cipher : " + cipher);

                int message = rsa.decrypt(cipher);
                System.out.println("Decrypted message : " + message);

                out.writeUTF(Integer.toString(message));
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