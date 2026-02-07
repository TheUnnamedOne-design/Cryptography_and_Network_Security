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
            
            System.out.print("Enter q: ");
            int q = Integer.parseInt(br.readLine());

            System.out.print("Enter alpha: ");
            int alpha = Integer.parseInt(br.readLine());

            System.out.print("Enter X_A: ");
            int X_A = Integer.parseInt(br.readLine());

            
            Elgamal temp = new Elgamal(q, alpha, X_A, true);
            int Y_A = temp.fast_exp(q, alpha, X_A);

            
            out.writeUTF(q + " " + alpha + " " + Y_A);

            Elgamal elgamal = new Elgamal(q, alpha, X_A, true);

            
            while (true) {
                String message = in.readUTF();
                System.out.println("Client sent : "+message);
                String[] C = message.split("\\s+");

                int c1 = Integer.parseInt(C[0]);
                int c2 = Integer.parseInt(C[1]);

                int M = elgamal.decrypt(c1, c2);
                System.out.println("Decryped Message : "+M);
                out.writeUTF(Integer.toString(M));
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