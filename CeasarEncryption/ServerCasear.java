import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;


class Caesar
{
    static int getCaesarindex(int v)
    {
        if(v<0) return 26+v;
        else return v%26;
    }

    static int getpos(char ch)
    {
        return ch-'a';
    }

    String CaesarEncrypt(String s)
    {
        String ans="";
        int i;
        for(i=0;i<s.length();i++){
            if(s.charAt(i)==' ') 
            {
                ans+=" ";
                continue;
            }
            int pos=getpos(s.charAt(i));
            char holder=(char)(97+getCaesarindex(pos+3));
            ans=ans+holder;
        }
        return ans;
    }
    String CaesarDecrypt(String s)
    {
        String ans="";
        int i;
        for(i=0;i<s.length();i++){
            if(s.charAt(i)==' ') 
            {
                ans+=" ";
                continue;
            }
            int pos=getpos(s.charAt(i));
            char holder=(char)(97+getCaesarindex(pos-3));
            ans=ans+holder;
        }
        return ans;
    }
}




public class ServerCasear
{

    Scanner sc=new Scanner(System.in);
    static DataInputStream in;
    static BufferedReader br;
    static DataOutputStream out;

    static void communicate(Socket socket)
    {
        
            
            try
            {
                in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out=new DataOutputStream(socket.getOutputStream());
                br=new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    
                    String hold=in.readUTF();
                    System.out.println("Client : "+hold);
                    System.out.print("Decoded : "+new Caesar().CaesarDecrypt(hold)+"\n");

                    out.writeUTF("Acknowledged");
                    
                }


            }   
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        
    }


    public static void main(String args[])
    {
        int pn=16000;
        try
        {
            ServerSocket serverSocket=new ServerSocket(pn);
            System.out.println("Server started and waiting for connection on port "+pn);
            Socket socket=serverSocket.accept();
            System.out.println("Connection established");
            communicate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}