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


    static String[][] SubstituteBytes(String matrix[][])
    {

        String[][] SBOX = {
        {"63","7C","77","7B","F2","6B","6F","C5","30","01","67","2B","FE","D7","AB","76"},
        {"CA","82","C9","7D","FA","59","47","F0","AD","D4","A2","AF","9C","A4","72","C0"},
        {"B7","FD","93","26","36","3F","F7","CC","34","A5","E5","F1","71","D8","31","15"},
        {"04","C7","23","C3","18","96","05","9A","07","12","80","E2","EB","27","B2","75"},
        {"09","83","2C","1A","1B","6E","5A","A0","52","3B","D6","B3","29","E3","2F","84"},
        {"53","D1","00","ED","20","FC","B1","5B","6A","CB","BE","39","4A","4C","58","CF"},
        {"D0","EF","AA","FB","43","4D","33","85","45","F9","02","7F","50","3C","9F","A8"},
        {"51","A3","40","8F","92","9D","38","F5","BC","B6","DA","21","10","FF","F3","D2"},
        {"CD","0C","13","EC","5F","97","44","17","C4","A7","7E","3D","64","5D","19","73"},
        {"60","81","4F","DC","22","2A","90","88","46","EE","B8","14","DE","5E","0B","DB"},
        {"E0","32","3A","0A","49","06","24","5C","C2","D3","AC","62","91","95","E4","79"},
        {"E7","C8","37","6D","8D","D5","4E","A9","6C","56","F4","EA","65","7A","AE","08"},
        {"BA","78","25","2E","1C","A6","B4","C6","E8","DD","74","1F","4B","BD","8B","8A"},
        {"70","3E","B5","66","48","03","F6","0E","61","35","57","B9","86","C1","1D","9E"},
        {"E1","F8","98","11","69","D9","8E","94","9B","1E","87","E9","CE","55","28","DF"},
        {"8C","A1","89","0D","BF","E6","42","68","41","99","2D","0F","B0","54","BB","16"}
        };

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                String holder=matrix[i][j];
                int lv=Integer.parseInt(String.valueOf(holder.charAt(0)),16);
                int rv=Integer.parseInt(String.valueOf(holder.charAt(1)),16);

                matrix[i][j]=SBOX[lv][rv];
            }
        }
        return matrix;
    }
    

    static String[][] ShiftRows(String matrix[][])
    {
        for(int i=1;i<4;i++)
        {
            int d=i%4;
            
            for(int j=0,k=d-1;j<k;j++,k--)
            {
                String t=matrix[i][j];
                matrix[i][j]=matrix[i][k];
                matrix[i][k]=t;
            }
            for(int j=d,k=4-1;j<k;j++,k--)
            {
                String t=matrix[i][j];
                matrix[i][j]=matrix[i][k];
                matrix[i][k]=t;
            }
            for(int j=0,k=4-1;j<k;j++,k--)
            {
                String t=matrix[i][j];
                matrix[i][j]=matrix[i][k];
                matrix[i][k]=t;
            }
        }
        return matrix;
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

            String matrix[][]=StateArray(plaintext,keyHex);

            
            matrix=SubstituteBytes(matrix);

            
            
             matrix=ShiftRows(matrix);
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    System.out.print(matrix[i][j]+" ");
                }
                System.out.println();
            }
                
            return encryptedtext;
        }
            
            
        }