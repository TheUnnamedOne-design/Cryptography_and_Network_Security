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
            
            try
            {
                while (true) { 
                    System.out.print("Enter q: ");
                    int q=Integer.parseInt(br.readLine());

                    System.out.print("Enter message: ");
                    int message=Integer.parseInt(br.readLine());

                    System.out.print("Enter alpha : ");
                    int alpha=Integer.parseInt(br.readLine());

                    System.out.print("Enter Y_A : ");
                    int Y_A=Integer.parseInt(br.readLine());


                   Elgamal elgamal=new Elgamal(q,alpha,Y_A);
                    int arr[]=(elgamal.encrypt(message));

                    s=Integer.valueOf(arr[0]).toString()+" "+Integer.valueOf(arr[1]).toString();


                    out.writeUTF(s);
                    System.out.println("Client Sent Message : "+s);
                    s=in.readUTF();
                    System.out.println("Server Decrypted Reply : "+s);
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