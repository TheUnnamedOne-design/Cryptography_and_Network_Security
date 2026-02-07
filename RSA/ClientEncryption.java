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
           
            String pub = in.readUTF();
            String[] P = pub.split("\\s+");

            int n = Integer.parseInt(P[0]);
            int e = Integer.parseInt(P[1]);

            RSA rsa = new RSA(n, e);

            
            while (true) {
                System.out.print("Enter message: ");
                int message = Integer.parseInt(br.readLine());

                int cipher = rsa.encrypt(message);
                out.writeUTF(Integer.toString(cipher));
                System.out.println("Sent : " + cipher);

                String reply = in.readUTF();
                System.out.println("Server decrypted: " + reply);
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