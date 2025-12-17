import java.io.*;
import java.net.*;
import java.util.*;



class PlayFair
{
    static String key="MONARCHY";
    static Map<Character, Integer[]> mp=new HashMap<>();
    static char[][] matrix=new char[5][5];

    public PlayFair() {
        char alphabets[]=new char[26];
        int i,j;
        Set<Character> set=new HashSet<>();

        for(i=0;i<key.length();i++) 
            {
                if(!set.contains(key.charAt(i)))
                {
                    alphabets[i]=key.charAt(i);
                    set.add(key.charAt(i));
                }
            }

        int ind=key.length();
        for(i=0;i<26;i++)
        {
            char hold=(char)((int)'A'+i);
            if(set.contains(hold)||hold=='J')
            {
                continue;
            }
            alphabets[ind]=hold;
            ind++;
        }
        ind=0;
        for(i=0;i<5;i++)
        {
            for(j=0;j<5;j++)
            {
                Integer details[]={i,j};
                mp.put(alphabets[ind],details);

                matrix[i][j]=alphabets[ind];

                //System.out.println(alphabets[ind]+" -> "+"("+details[0]+","+details[1]+")");
                ind++;
            }
        }

    }



    public String encrypt(String s)
    {
        s=s.trim();
        String ans="";
        s=s.toUpperCase();
        s = s.replaceAll("\\s","");


        if(s.length()%2==1) s+="Z";
        String Sc="";

        for(int i=0;i<s.length();i++) 
        {
            if(s.charAt(i)=='J') Sc+='I';
            else Sc+=s.charAt(i);
        }

        for(int i=0;i<s.length();i=i+2)
        {
            char ch1=Sc.charAt(i);
            char ch2=Sc.charAt(i+1);

            Integer p1[]=mp.get(ch1);
            Integer p2[]=mp.get(ch2);

            int r1=p1[0],c1=p1[1],r2=p2[0],c2=p2[1];
            if(r1==r2)
            {
                int v1=(c1+1)%5;
                int v2=(c2+1)%5;
                ans+="" +matrix[r1][v1]+matrix[r1][v2];
            }
            else if(c1==c2)
            {
                int v1=(r1+1)%5;
                int v2=(r2+1)%5;
                ans+="" +matrix[v1][c1]+matrix[v2][c2];
            }
            else
            {
                ans+="" +matrix[r1][c2]+matrix[r2][c1];
            }
        }

        return ans;
    }



    public String decrypt(String s)
    {
        s=s.trim();
        String ans="";
        s=s.toUpperCase();
        s = s.replaceAll("\\s","");


        if(s.length()%2==1) s+="Z";
        String Sc="";

        for(int i=0;i<s.length();i++) 
        {
            if(s.charAt(i)=='J') Sc+='I';
            else Sc+=s.charAt(i);
        }

        for(int i=0;i<s.length();i=i+2)
        {
            char ch1=Sc.charAt(i);
            char ch2=Sc.charAt(i+1);

            Integer p1[]=mp.get(ch1);
            Integer p2[]=mp.get(ch2);

            int r1=p1[0],c1=p1[1],r2=p2[0],c2=p2[1];
            if(r1==r2)
            {
                int v1=(c1-1)%5;
                v1=(v1<0)?(5+v1):v1;
                int v2=(c2-1)%5;
                v2=(v2<0)?(5+v2):v2;
                ans+="" +matrix[r1][v1]+matrix[r1][v2];
            }
            else if(c1==c2)
            {
                int v1=(r1-1)%5;
                v1=(v1<0)?(5+v1):v1;
                int v2=(r2-1)%5;
                v2=(v2<0)?(5+v2):v2;
                ans+="" +matrix[v1][c1]+matrix[v2][c2];
            }
            else
            {
                ans+="" +matrix[r1][c2]+matrix[r2][c1];
            }
        }

        return ans;
    }

    

}

public class ClientPlayFair
{

    private static void communicate(Socket socket)
    {
        try
        {
            DataInputStream in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

            PlayFair pf=new PlayFair();

            String hold="";
            while(true)
            {

                hold=br.readLine();
                hold=pf.encrypt(hold);
                out.writeUTF(hold);
                System.out.println("Client : "+hold);

                hold=in.readUTF();
                System.out.println("Server : "+hold);


            }
           }
        catch(Exception e) 
        {
            e.printStackTrace();
        }

    }

    public static void main(String args[])
    {
        int pn=8000;

        System.out.println("Listening on port "+pn);
        try
        {
            Socket socket=new Socket("127.0.0.1",pn);
            System.out.println("Connected to server");
            communicate(socket);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}