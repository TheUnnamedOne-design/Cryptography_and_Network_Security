import java.nio.charset.*;
public class AES
{
    static String SBOXFunction(int i,int j)
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
        return SBOX[i][j];
    }


    static String InvSBOXFunction(int i,int j)
    {
        String[][] INV_SBOX = {
            {"52","09","6A","D5","30","36","A5","38","BF","40","A3","9E","81","F3","D7","FB"},
            {"7C","E3","39","82","9B","2F","FF","87","34","8E","43","44","C4","DE","E9","CB"},
            {"54","7B","94","32","A6","C2","23","3D","EE","4C","95","0B","42","FA","C3","4E"},
            {"08","2E","A1","66","28","D9","24","B2","76","5B","A2","49","6D","8B","D1","25"},
            {"72","F8","F6","64","86","68","98","16","D4","A4","5C","CC","5D","65","B6","92"},
            {"6C","70","48","50","FD","ED","B9","DA","5E","15","46","57","A7","8D","9D","84"},
            {"90","D8","AB","00","8C","BC","D3","0A","F7","E4","58","05","B8","B3","45","06"},
            {"D0","2C","1E","8F","CA","3F","0F","02","C1","AF","BD","03","01","13","8A","6B"},
            {"3A","91","11","41","4F","67","DC","EA","97","F2","CF","CE","F0","B4","E6","73"},
            {"96","AC","74","22","E7","AD","35","85","E2","F9","37","E8","1C","75","DF","6E"},
            {"47","F1","1A","71","1D","29","C5","89","6F","B7","62","0E","AA","18","BE","1B"},
            {"FC","56","3E","4B","C6","D2","79","20","9A","DB","C0","FE","78","CD","5A","F4"},
            {"1F","DD","A8","33","88","07","C7","31","B1","12","10","59","27","80","EC","5F"},
            {"60","51","7F","A9","19","B5","4A","0D","2D","E5","7A","9F","93","C9","9C","EF"},
            {"A0","E0","3B","4D","AE","2A","F5","B0","C8","EB","BB","3C","83","53","99","61"},
            {"17","2B","04","7E","BA","77","D6","26","E1","69","14","63","55","21","0C","7D"}
        };

        return INV_SBOX[i][j];
    }

    public byte[] StringToBytes(String a)
    {
        return a.getBytes(StandardCharsets.UTF_8);
    }

    public String BytesToHex(byte[] arr)
    {
        String ans="";
        for(int i=0;i<arr.length;i++)
        {
            String hold=String.format("%02X", arr[i]);
            ans+=hold;
        }
        return ans;
    }


    public byte[] HexToBytes(String a)
    {
        int n=a.length();
        byte arr[]=new byte[n/2];
        for(int i=0;i<a.length();i+=2)
        {
            arr[i/2]=(byte)Integer.parseInt(a.substring(i,i+2),16);
        }
        return arr;
    }

    public String ByteToString(byte[] arr)
    {
        return new String(arr,StandardCharsets.UTF_8);
    }


    public byte[] EnsureLength(byte[] arr, int n)
    {
        if(arr.length<n)
        {
            byte bytes[]=new byte[n];
            System.arraycopy(arr, 0, bytes, 0, arr.length);
            return bytes;
        }
        else if(arr.length>n)
        {
            byte bytes[]=new byte[n];
            System.arraycopy(arr, 0, bytes, 0, n);
            return bytes;
        }
        return arr;
    }

    public String[][] MakeMatrix(String s)
    {
        String mat[][]=new String[4][4];
        int ctr=0;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                mat[j][i]=s.substring(ctr,ctr+2);
                ctr+=2;
            }
        }
        return mat;
    }


    String[] f_function(String word[],int round)
    {
        String temp[]=new String[4];
        for(int i=0;i<4;i++)
        {
            temp[i]=word[(i+1)%4];
        }
        for(int i=0;i<4;i++)
        {
            int row=Integer.parseInt(String.valueOf(temp[i].charAt(0)),16);
            int col=Integer.parseInt(String.valueOf(temp[i].charAt(1)),16);
            temp[i]=SBOXFunction(row,col);
        }
        int Rcon[]={0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1B,0x36};
        int v=Integer.parseInt(temp[0],16);
        v^=Rcon[round-1];
        temp[0]=String.format("%02X",v);
        for(int i=0;i<4;i++)
        {
            v=Integer.parseInt(temp[i],16);
            temp[i]=String.format("%02x", v);
        }

        return temp;
    }


    public String[] XORWord(String a[],String b[])
    {
        String temp[]=new String[4];
        //System.out.println("hello");
        for(int i=0;i<4;i++)
        {
            int av=Integer.parseInt(a[i],16);
            int bv=Integer.parseInt(b[i],16);

            int v=av^bv;
            temp[i]=String.format("%02X",v);
            //System.out.println("Hello : "+temp[i]);
        }

        return temp;
    }


    public String[][][] KeyExpansion(String keyHex)
    {
        String ans[][][]=new String[11][4][4];
        String words[][]=new String[44][4];
        int ctr=0;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                words[i][j]=keyHex.substring(ctr,ctr+2);
                ctr+=2;
            }
        }

        ans[0]=MakeMatrix(keyHex);
        //System.out.println("HELLO");
        for(int round=1;round<=10;round++)
            {
                int current=4*(round-1);
                int currentEnd=4*round-1;
                int next=4*(round);
                
                //System.out.println("HELLO");
                String w4a[]=f_function(words[currentEnd],round);
                //String w4a[]={"12","45","78","42"};
                words[next]=XORWord(w4a, words[current]);
                words[next+1]=XORWord(words[current+1], words[next]);
                words[next+2]=XORWord(words[current+2], words[next+1]);
                words[next+3]=XORWord(words[current+3], words[next+2]);
                //System.out.println("HELLO");
        }

        for(int i=0;i<11;i++) {
            for(int j=0;j<4;j++) {
                for(int k=0;k<4;k++) {
                    ans[i][k][j] = words[4*i + j][k];
                }
            }
        }
        return ans;
    }


    void printMatrix(String mat[][])
    {
        int m=mat.length;
        int n=mat[0].length;

        for(int i=0;i<m;i++)
        {
            for(int j=0;j<n;j++)
            {
                System.out.print(mat[i][j]+" ");
            }
            System.out.println();
        }
        
    }


    public String[][] StateXOR(String a[][], String b[][])
    {
        String ans[][]=new String[4][4];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int av=Integer.parseInt(a[i][j],16);
                int bv=Integer.parseInt(b[i][j],16);
                int v=av^bv;
                ans[i][j]=String.format("%02X", v);
            }
        }
        return ans;
    }

    public String[][] SubBytes(String state[][])
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int row=Integer.parseInt(String.valueOf(state[i][j].charAt(0)),16);
                int col=Integer.parseInt(String.valueOf(state[i][j].charAt(1)),16);

                state[i][j]=SBOXFunction(row, col);
            }
        }

        return state;
    }


    public String[][] ShiftRows(String state[][])
    {
        String temp[][]=new String[4][4];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                temp[i][j]=state[i][(i+j)%4];
            }
        }
        return temp;
    }




    public String[][] MixColumns(String state[][])
    {
        int mat[][]={{2,3,1,1},
                     {1,2,3,1},
                     {1,1,2,3},
                     {3,1,1,2}
                    };
        
        String temp[][]=new String[4][4];
        int irreducible=0x11b;
        
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int ansHold=0;
                for(int k=0;k<4;k++)
                {
                    int av=mat[i][k];
                    int bv=Integer.parseInt(state[k][j],16);
                    int prod=0;

                    if(av==1)
                    {
                        prod=bv;
                    }
                    else if(av==2)
                    {
                        prod=bv<<1;
                        if(prod>0xff)
                        {
                            prod=prod^irreducible;
                        }
                    }
                    else if(av==3)
                    {
                        prod=bv<<1;
                        if(prod>0xff)
                        {
                            prod=prod^irreducible;
                        }
                        prod^=bv;
                    }

                    ansHold^=prod;
                }

                temp[i][j]=String.format("%02X",ansHold);
            }
        }

        return temp;
    }


    public String encrypt(String plaintext, String key)
    {
        String ans="";

        byte[] pt=StringToBytes(plaintext);
        byte[] keyBytes=StringToBytes(key);

        pt=EnsureLength(pt, 16);
        keyBytes=EnsureLength(keyBytes, 16);

        String TextHex=BytesToHex(pt);
        String keyHex=BytesToHex(keyBytes);

        String PtMat[][]=MakeMatrix(TextHex);
        String AllKeys[][][]=KeyExpansion(keyHex);

        String key0[][]=AllKeys[0];

        String state[][]=PtMat;
        state=StateXOR(state, key0);

        for(int i=1;i<=9;i++)
        {
            state=SubBytes(state);
            state=ShiftRows(state);
            state=MixColumns(state);
            state=StateXOR(state, AllKeys[i]);
        }


        state=SubBytes(state);
        state=ShiftRows(state);
        state=StateXOR(state, AllKeys[10]);


        StringBuilder sb=new StringBuilder();
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                sb.append(state[j][i]);
            }
        }

        return sb.toString();
    }


    public String[][] InvShiftRows(String state[][])
    {
        String temp[][]=new String[4][4];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                temp[i][j]=state[i][(4+j-i)%4];
            }
        }
        return temp;
    }


    public String[][] InvSubBytes(String state[][])
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int row=Integer.parseInt(String.valueOf(state[i][j].charAt(0)),16);
                int col=Integer.parseInt(String.valueOf(state[i][j].charAt(1)),16);

                state[i][j]=InvSBOXFunction(row, col);
            }
        }

        return state;
    }


    public String[][] InvMixColumns(String state[][])
    {
        int mat[][]={{14,11,13,9},
                     {9,14,11,13},
                     {13,9,14,11},
                     {11,13,9,14}
                    };
        
        String temp[][]=new String[4][4];
        int irreducible=0x11b;
        
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int ansHold=0;
                for(int k=0;k<4;k++)
                {
                    int av=mat[i][k];
                    int bv=Integer.parseInt(state[k][j],16);
                    int prod=0;

                    prod=galoisMult(av, bv, irreducible);
                    ansHold^=prod;
                }

                temp[i][j]=String.format("%02X",ansHold);
            }
        }

        return temp;
    }


    public int galoisMult(int a, int b, int irreducible)
    {
        int prod=0;
        int temp_b=b;
        
        for(int i=0;i<8;i++)
        {
            if((a & 1) != 0)
            {
                prod^=temp_b;
            }
            
            int high_bit=(temp_b & 0x80);
            temp_b=(temp_b << 1) & 0xff;
            
            if(high_bit != 0)
            {
                temp_b^=irreducible;
            }
            
            a>>=1;
        }
        
        return prod & 0xff;
    }


    public String decrypt(String ciphertext, String key)
    {
        String ans="";

        byte[] ct=HexToBytes(ciphertext);
        byte[] keyBytes=StringToBytes(key);

        ct=EnsureLength(ct, 16);
        keyBytes=EnsureLength(keyBytes, 16);

        String TextHex=BytesToHex(ct);
        String keyHex=BytesToHex(keyBytes);

        String CtMat[][]=MakeMatrix(TextHex);
        String AllKeys[][][]=KeyExpansion(keyHex);

        String state[][]=CtMat;
        state=StateXOR(state, AllKeys[10]);

        for(int i=9;i>=1;i--)
        {
            state=InvShiftRows(state);
            state=InvSubBytes(state);
            state=StateXOR(state, AllKeys[i]);
            state=InvMixColumns(state);
        }

        state=InvShiftRows(state);
        state=InvSubBytes(state);
        state=StateXOR(state, AllKeys[0]);

        StringBuilder sb=new StringBuilder();
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                sb.append(state[j][i]);
            }
        }

        String plainHex=sb.toString();
        byte[] plain=HexToBytes(plainHex);
        return ByteToString(plain);
    }
}