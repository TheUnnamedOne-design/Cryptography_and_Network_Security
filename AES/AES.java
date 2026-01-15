public class AES
{
    

    
    static String textToHex(String text)
    {
        
        if(text.length() < 16)
        {
            
            text = String.format("%-16s", text);
        }
        else if(text.length() > 16)
        {
            
            text = text.substring(0, 16);
        }
        
        String hex = "";
        for(int i=0; i<text.length(); i++)
        {
            String charHex = String.format("%2s", Integer.toHexString(text.charAt(i))).replaceAll(" ", "0");
            hex += charHex;
        }
        return hex;
    }
    
    
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

    static String BinaryXOR(String a,String b)
    {
        String ans="";
        a=String.format("%8s", a).replaceAll(" ", "0");
        b=String.format("%8s", b).replaceAll(" ", "0");
        for(int i=0;i<a.length();i++)
        {
            if(a.charAt(i)!=b.charAt(i)) ans+="1";
            else ans+="0";
        }
        return ans;
    }


    static String HexToBinary(String a)
    {
        int val=Integer.parseInt(a,16);
        String b=String.format("%8s", Integer.toBinaryString(val)).replaceAll(" ", "0");
        if(b.length()>8)
        {
            b=b.substring(b.length()-8);
        }
        return b;
    }


    static String[][] StateArray(String pt,String key)
    {
        String stateMatrix[][]=new String[4][4];
        int i=0;
        for(int k=0;k<4;k++)
        {
            for(int j=0;j<4;j++)
            {
                String as1=HexToBinary(String.valueOf(pt.charAt(i)));
                String as2=HexToBinary(String.valueOf(pt.charAt(i+1)));
                String bs1=HexToBinary(String.valueOf(key.charAt(i)));
                String bs2=HexToBinary(String.valueOf(key.charAt(i+1)));

                String f1=BinaryXOR(as1,bs1);
                String f2=BinaryXOR(as2,bs2);

                int ans1=Integer.parseInt(f1,2);
                int ans2=Integer.parseInt(f2,2);

                String fans1=Integer.toHexString(ans1);
                String fans2=Integer.toHexString(ans2);

                String completeString=fans1+fans2;

                stateMatrix[j][k]=completeString;
                i+=2;

            }
        }
        return stateMatrix;
    }

    
    
    String encrypt(String message)
    {
            String messages[]=message.split("\\s+");
            String plaintextInput=messages[0];
            String keyInput=messages[1];
            String encryptedtext="";
            
            
            String plaintext = textToHex(plaintextInput);
            String keyHex = textToHex(keyInput);
        
            System.out.println("plaintext : "+plaintext+"| KeyHex : "+keyHex);

            String getStateArray[][]=StateArray(plaintext,keyHex);

            // for(int i=0;i<4;i++)
            // {
            //     for(int j=0;j<4;j++)
            //     {
            //         System.out.print(getStateArray[i][j]+" ");
            //     }
            //     System.out.println();
            // }
                
            return encryptedtext;
        }
            
            
        }