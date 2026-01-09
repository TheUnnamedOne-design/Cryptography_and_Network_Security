import java.util.*;
public class DESCompleteEncryption
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

        int StraightPBox[]={57, 49, 41, 33, 25, 17, 9,
                            1, 58, 50, 42, 34, 26, 18,
                            10, 2, 59, 51, 43, 35, 27,
                            19, 11, 3, 60, 52, 44, 36,
                            63, 55, 47, 39, 31, 23, 15,
                            7, 62, 54, 46, 38, 30, 22,
                            14, 6, 61, 53, 45, 37, 29,
                            21, 13, 5, 28, 20, 12, 4};
        int CompressionPBox[]={14, 17, 11, 24, 1, 5,
                               3, 28, 15, 6, 21, 10,
                               23, 19, 12, 4, 26, 8,
                               16, 7, 27, 20, 13, 2,
                               41, 52, 31, 37, 47, 55,
                               30, 40, 51, 45, 33, 48,
                               44, 49, 39, 56, 34, 53,
                               46, 42, 50, 36, 29, 32};

        String str="";
        for(int i=0;i<StraightPBox.length;i++)
        {
            str+=s.charAt(StraightPBox[i]-1);
        }
        s=str;
        int n=s.length();

       
        int[] shiftSchedule = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
        
        for(int round=0; round<16; round++)
        {
            String half1=shift(s.substring(0,n/2), shiftSchedule[round]);
            String half2=shift(s.substring(n/2,n), shiftSchedule[round]);
            s=half1+half2;
            
            str="";
            for(int i=0;i<CompressionPBox.length;i++)
            {
                str+=s.charAt(CompressionPBox[i]-1);
            }
            keys.add(str);
            System.out.println("Round "+(round+1)+" key : "+str);
        }
        return keys;
    }


    static String F(String p,String k)
    {
        int expansion_permutation[]={32, 1, 2, 3, 4, 5,
                                     4, 5, 6, 7, 8, 9,
                                     8, 9, 10, 11, 12, 13,
                                     12, 13, 14, 15, 16, 17,
                                     16, 17, 18, 19, 20, 21,
                                     20, 21, 22, 23, 24, 25,
                                     24, 25, 26, 27, 28, 29,
                                     28, 29, 30, 31, 32, 1};
        int P4[]={16, 7, 20, 21,
                  29, 12, 28, 17,
                  1, 15, 23, 26,
                  5, 18, 31, 10,
                  2, 8, 24, 14,
                  32, 27, 3, 9,
                  19, 13, 30, 6,
                  22, 11, 4, 25};

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

        int[][] S1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
        };

        int[][] S2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
        };

        int[][] S3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
        };

        int[][] S4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
        };

        int[][] S5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
        };

        int[][] S6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
        };

        int[][] S7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
        };

        int[][] S8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
        };

        
        
        String box1=xored.substring(0,6);
        String box2=xored.substring(6,12);
        String box3=xored.substring(12,18);
        String box4=xored.substring(18,24);
        String box5=xored.substring(24,30);
        String box6=xored.substring(30,36);
        String box7=xored.substring(36,42);
        String box8=xored.substring(42,48);

        String sr=""+box1.charAt(0)+box1.charAt(5);
        int r1=Integer.parseInt(sr,2);
        String sc=""+box1.charAt(1)+box1.charAt(2)+box1.charAt(3)+box1.charAt(4);
        int c1=Integer.parseInt(sc,2);
        String val1=String.format("%4s",Integer.toBinaryString(S1[r1][c1])).replaceAll(" ", "0");

        sr=""+box2.charAt(0)+box2.charAt(5);
        int r2=Integer.parseInt(sr,2);
        sc=""+box2.charAt(1)+box2.charAt(2)+box2.charAt(3)+box2.charAt(4);
        int c2=Integer.parseInt(sc,2);
        String val2=String.format("%4s",Integer.toBinaryString(S2[r2][c2])).replaceAll(" ", "0");

        sr=""+box3.charAt(0)+box3.charAt(5);
        int r3=Integer.parseInt(sr,2);
        sc=""+box3.charAt(1)+box3.charAt(2)+box3.charAt(3)+box3.charAt(4);
        int c3=Integer.parseInt(sc,2);
        String val3=String.format("%4s",Integer.toBinaryString(S3[r3][c3])).replaceAll(" ", "0");

        sr=""+box4.charAt(0)+box4.charAt(5);
        int r4=Integer.parseInt(sr,2);
        sc=""+box4.charAt(1)+box4.charAt(2)+box4.charAt(3)+box4.charAt(4);
        int c4=Integer.parseInt(sc,2);
        String val4=String.format("%4s",Integer.toBinaryString(S4[r4][c4])).replaceAll(" ", "0");

        sr=""+box5.charAt(0)+box5.charAt(5);
        int r5=Integer.parseInt(sr,2);
        sc=""+box5.charAt(1)+box5.charAt(2)+box5.charAt(3)+box5.charAt(4);
        int c5=Integer.parseInt(sc,2);
        String val5=String.format("%4s",Integer.toBinaryString(S5[r5][c5])).replaceAll(" ", "0");

        sr=""+box6.charAt(0)+box6.charAt(5);
        int r6=Integer.parseInt(sr,2);
        sc=""+box6.charAt(1)+box6.charAt(2)+box6.charAt(3)+box6.charAt(4);
        int c6=Integer.parseInt(sc,2);
        String val6=String.format("%4s",Integer.toBinaryString(S6[r6][c6])).replaceAll(" ", "0");

        sr=""+box7.charAt(0)+box7.charAt(5);
        int r7=Integer.parseInt(sr,2);
        sc=""+box7.charAt(1)+box7.charAt(2)+box7.charAt(3)+box7.charAt(4);
        int c7=Integer.parseInt(sc,2);
        String val7=String.format("%4s",Integer.toBinaryString(S7[r7][c7])).replaceAll(" ", "0");

        sr=""+box8.charAt(0)+box8.charAt(5);
        int r8=Integer.parseInt(sr,2);
        sc=""+box8.charAt(1)+box8.charAt(2)+box8.charAt(3)+box8.charAt(4);
        int c8=Integer.parseInt(sc,2);
        String val8=String.format("%4s",Integer.toBinaryString(S8[r8][c8])).replaceAll(" ", "0");

        String holder=val1+val2+val3+val4+val5+val6+val7+val8;

        String ans="";

        for(int i=0;i<P4.length;i++)
        {
            ans+=holder.charAt(P4[i]-1);
        }

        return ans;
    }


    // Convert text to 64-bit binary (8 characters = 64 bits)
    static String textToBinary(String text)
    {
        // Pad or truncate to exactly 8 characters
        if(text.length() < 8)
        {
            // Pad with spaces
            text = String.format("%-8s", text);
        }
        else if(text.length() > 8)
        {
            // Truncate to 8 characters
            text = text.substring(0, 8);
        }
        
        String binary = "";
        for(int i=0; i<text.length(); i++)
        {
            String charBinary = String.format("%8s", Integer.toBinaryString(text.charAt(i))).replaceAll(" ", "0");
            binary += charBinary;
        }
        return binary;
    }
    
    // Convert 64-bit binary to text (8 characters)
    static String binaryToText(String binary)
    {
        String text = "";
        for(int i=0; i<binary.length(); i+=8)
        {
            String byteStr = binary.substring(i, i+8);
            int charCode = Integer.parseInt(byteStr, 2);
            text += (char)charCode;
        }
        return text;
    }

    static String f_key(String word,String key)
    {
        String L=word.substring(0,32);
        String R=word.substring(32);

        String ans="";
        String F_result=F(R,key);

        String NL="";
        for(int i=0;i<32;i++)
        {
            if(F_result.charAt(i)!=L.charAt(i)) NL+="1";
            else NL+="0";
        }

        ans=NL+R;
        return ans;
    }




    
    
    
    String encrypt(String message)
    {
        String messages[]=message.split("\\s+");
        String plaintextInput=messages[0];
        String keyInput=messages[1];
        
        // Convert text to 64-bit binary
        String plaintext = textToBinary(plaintextInput);
        String keyBinary = textToBinary(keyInput);
        
        ArrayList<String> keys=key_generate(keyBinary);
        String encryptedtext="";
        
        int initial_permutation[]={
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
        };
        
        int final_permutation[]={
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
        };
        
        
        String s1="";
        
        for(int i=0;i<initial_permutation.length;i++)
            {
                s1+=plaintext.charAt(initial_permutation[i]-1);
            }
            
            
            String current=s1;
            
            
            for(int round=0; round<16; round++)
            {
                String temp=f_key(current, keys.get(round));
                if(round<15) 
                {
                    current=temp.substring(32)+temp.substring(0,32);
                }
                else
                {
                    current=temp;
                }
            }
            
            
            for(int i=0;i<final_permutation.length;i++)
                {
                    encryptedtext+=current.charAt(final_permutation[i]-1);
                }
                
                return encryptedtext+" "+keyBinary;
            }
            




            String decrypt(String message)
            {
               String messages[]=message.split("\\s+");
                String ciphertextBinary=messages[0];
                String keyBinary=messages[1];

                
                ArrayList<String> keys=key_generate(keyBinary);
                String decryptedtext="";

                int initial_permutation[]={
                    58, 50, 42, 34, 26, 18, 10, 2,
                    60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6,
                    64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17, 9, 1,
                    59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5,
                    63, 55, 47, 39, 31, 23, 15, 7
                };
                
                int final_permutation[]={
                    40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41, 9, 49, 17, 57, 25
                };


                String s1="";

                for(int i=0;i<initial_permutation.length;i++)
                {
                    s1+=ciphertextBinary.charAt(initial_permutation[i]-1);
                }


                String current=s1;
            
                
                for(int round=0; round<16; round++)
                {
                    String temp=f_key(current, keys.get(15-round));
                    if(round<15) 
                    {
                        current=temp.substring(32)+temp.substring(0,32);
                    }
                    else 
                    {
                        current=temp;
                    }
                }

                
                for(int i=0;i<final_permutation.length;i++)
                {
                    decryptedtext+=current.charAt(final_permutation[i]-1);
                }
                
                // Convert binary back to text
                String decryptedTextOutput = binaryToText(decryptedtext);
                return decryptedTextOutput;
            }
            
            
        }