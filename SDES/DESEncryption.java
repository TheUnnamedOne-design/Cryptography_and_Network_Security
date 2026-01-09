import java.util.*;
public class DESEncryption
{

    static String shift(String s,int k)
    {
        int n=s.length();
        k=k%n;

        String ans=s.substring(k)+s.substring(0,k);
        return ans;
    }

    static ArrayList<String> key_generate(String s)
    {
        ArrayList<String> keys=new ArrayList<>();

        int StraightPBox[]={3,5,2,7,4,10,1,9,8,6};
        int CompressionPBox[]={6,3,7,4,8,5,10,9};

        String str="";
        for(int i=0;i<StraightPBox.length;i++)
        {
            str+=s.charAt(StraightPBox[i]-1);
        }
        s=str;
        int n=s.length();

        String half1=shift(s.substring(0,n/2),1);
        String half2=shift(s.substring(n/2,n),1);

        s=half1+half2;
        str="";
        for(int i=0;i<CompressionPBox.length;i++)
        {
            str+=s.charAt(CompressionPBox[i]-1);
        }

        String rk1=str;
        System.out.println("Round 1 key : "+str);
        keys.add(rk1);

        half1=shift(s.substring(0,n/2),2);
        half2=shift(s.substring(n/2,n),2);
        s=half1+half2;
        str="";
        for(int i=0;i<CompressionPBox.length;i++)
        {
            str+=s.charAt(CompressionPBox[i]-1);
        }
        String rk2=str;
        keys.add(rk2);
        System.out.println("Round 2 key "+str);
        return keys;
    }


    static String F(String p,String k)
    {
        int expansion_permutation[]={4,1,2,3,2,3,4,1};
        int P4[]={2,4,3,1};

        String permuted="";
        for(int i=0;i<expansion_permutation.length;i++)
        {
            permuted+=p.charAt(expansion_permutation[i]-1);
        }

        //System.out.println("initial : "+permuted);

        String xored="";
        for(int i=0;i<k.length();i++)
        {
            if(k.charAt(i)!=permuted.charAt(i)) xored+="1";
            else xored+="0";
        }
        //System.out.println("xored : "+xored);

        int[][] S0 = {
                        {1, 0, 3, 2},
                        {3, 2, 1, 0},
                        {0, 2, 1, 3},
                        {3, 1, 3, 2}
                    };

        int[][] S1 = {
                        {0, 1, 2, 3},
                        {2, 0, 1, 3},
                        {3, 0, 1, 0},
                        {2, 1, 0, 3}
                    };

        
        String left=xored.substring(0,4);
        String right=xored.substring(4); 

        String sr=""+left.charAt(0)+left.charAt(3);
        int lr=Integer.parseInt(sr,2);
        String sc=""+left.charAt(1)+left.charAt(2)+"";
        int lc=Integer.parseInt(sc,2);
        String lValue=String.format("%2s",Integer.toBinaryString(S0[lr][lc])).replaceAll(" ", "0");


        sr=""+right.charAt(0)+right.charAt(3);
        int rr=Integer.parseInt(sr,2);
        sc=""+right.charAt(1)+right.charAt(2);
        int rc=Integer.parseInt(sc,2);
        String rValue=String.format("%2s",Integer.toBinaryString(S1[rr][rc])).replaceAll(" ", "0");

        String holder=lValue+rValue;

        String ans="";

        for(int i=0;i<P4.length;i++)
        {
            ans+=holder.charAt(P4[i]-1);
        }

        return ans;
    }


    static String f_key(String word,String key)
    {
        String L=word.substring(0,4);
        String R=word.substring(4);

        String ans="";
        String F_result=F(R,key);

        String NL="";
        for(int i=0;i<4;i++)
        {
            if(F_result.charAt(i)!=L.charAt(i)) NL+="1";
            else NL+="0";
        }

        ans=NL+R;
        return ans;
    }




    String decrypt(String s)
    {
        String ans="";
        return ans;
    }



    String encrypt(String message)
    {
        String messages[]=message.split("\\s+");
        String plaintext=messages[0];
        String ciphertext=messages[1];

        
        ArrayList<String> keys=key_generate(ciphertext);
        String encryptedtext="";

        int initial_permutation[]={2,6,3,1,4,8,5,7};
        int final_permutation[]={4,1,3,5,7,2,8,6};


        String s1="";

        for(int i=0;i<initial_permutation.length;i++)
        {
            s1+=plaintext.charAt(initial_permutation[i]-1);
        }


        String s2=f_key(s1, keys.get(0));
       // System.out.println("f_k1 : "+s2);

        String s3=s2.substring(4)+s2.substring(0,4);

        String s4=f_key(s3,keys.get(1));

        //System.out.println("f_k2 : "+s4);


        for(int i=0;i<final_permutation.length;i++)
        {
            encryptedtext+=s4.charAt(final_permutation[i]-1);
        }
        
        return encryptedtext;
    }



}