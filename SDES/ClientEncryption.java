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
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            DataInputStream in=new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            String s="";
            SDESEncryption des=new SDESEncryption();

            try
            {
                while (true) { 
                System.out.print("Enter plain text: ");
                String s1=br.readLine();
                System.out.print("Enter key : ");
                String s2=br.readLine();
                s=s1+" "+s2;
                s=des.encrypt(s);
                out.writeUTF(s);
                System.out.println("Client : "+s);
                s=in.readUTF();
                System.out.println("Server : "+s);
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