import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

    static void operate(Socket socket)
    {
        try
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in=new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            ElgamalDSA elgamal=new ElgamalDSA(16,19,10);
            String s;

             try
            {
                while (true) {

                s=in.readUTF();
                System.out.println("Client Received : "+s);
                System.out.print("Enter key : ");
                String key=br.readLine();
                String[] parts = s.split("\\s+");
                String modifiedCiphertext = "Modified" + parts[0];
                s = modifiedCiphertext + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + key;
                
                s=elgamal.verify(s);
                out.writeUTF(s);
                System.out.println("Server Message : "+s);
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