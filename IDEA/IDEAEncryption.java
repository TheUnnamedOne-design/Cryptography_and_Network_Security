public class IDEAEncryption
{


    public static String CyclicShift(String s,int shift)
    {
        int n=s.length();
        shift=shift%n;
        return s.substring(n) + s.substring(0, n);
    }




    public static String[] key_generate(String key)
    {
        String keyParts[]=new String[52];

        int ct=0;


        for(int k=0;k<6;k++)
        {
            for(int i=0;i<key.length();i+=16)
            {
                String word=key.substring(i,i+16);
                keyParts[ct]=word;
                //System.out.print(word+" ");
                ct++;
            }
            key=CyclicShift(key, 25);
        }
            for(int i=0;i<key.length();i+=16)
            {
                String word=key.substring(i,i+16);
                keyParts[ct]=word;
                //System.out.print(word+" ");
                ct++;
                if(ct==52) break;
            }

        //System.out.println();
        //System.out.println("ct : "+ct);
        return keyParts;

    }


    public static String XOR(String s1,String s2,int length)
    {
        String ans="";
        for(int i=0;i<length;i++)
        {
            if(s1.charAt(i)!=s2.charAt(i)) ans+="1";
            else ans+="0";
        }
        return ans;
    }

    public static String MUL(String s1,String s2,int length)
    {
       String ans="";
        int v1=Integer.parseInt(s1,2);
        int v2=Integer.parseInt(s1,2);
        v1=(v1==0)?65536:v1;
        v2=(v2==0)?65536:v2;

        int res=(v1*v2)%65537;
        ans=String.format("%16s", Integer.toBinaryString(res)).replaceAll(" ", "0");
        return ans;
    }

    public static String ADD(String s1,String s2,int length)
    {
        String ans="";
        int v1=Integer.parseInt(s1,2);
        int v2=Integer.parseInt(s1,2);
        v1=(v1==0)?65536:v1;
        v2=(v2==0)?65536:v2;

        int res=(v1*v2)%65537;
        ans=String.format("%16s", Integer.toBinaryString(res)).replaceAll(" ", "0");
        return ans;
    }


    public static String[] EncryptRound(String pt[],String keys[],int start)
    {
        String Z[]=new String[6];
        for(int i=0;i<6;i++)
        {
            Z[i]=keys[i+start]; 
        }

    }


    public String encrypt(String text, String key)
    {
        if(text.length()>8) text=text.substring(0,8);
        if(key.length()>16) key=key.substring(0,16);


        text=String.format("%-8s", text).replaceAll(" ", "x");
        key=String.format("%-16s", key).replaceAll(" ", "x");

        System.out.println(text+" "+key);


        String s1="";
        String s2="";

        for(int i=0;i<text.length();i++)
        {
            int vs1=(int)(text.charAt(i));
            String v1=String.format("%8s",Integer.toBinaryString(vs1)).replaceAll(" ", "0");

            s1+=v1;
        }

        for(int i=0;i<key.length();i++)
        {
            int vs1=(int)(key.charAt(i));
            String v1=String.format("%8s",Integer.toBinaryString(vs1)).replaceAll(" ", "0");

            s2+=v1;
        }

        text=s1;
        key=s2;


        String pt[]=new String[4];
        int ct=0;
        for(int i=0;i<text.length();i+=16)
        {
            String v1=text.substring(i,i+16);
            pt[ct]=v1;
            System.out.print(pt[ct]+" ");
            ct++;
        }
        System.out.println();

        String keys[]=key_generate(key);
        String Cipherresult[]=EncryptRound(String pt[],String keys[],int start);



        String ans="";
        return ans;

    }
}