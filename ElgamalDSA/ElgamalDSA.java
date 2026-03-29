import java.math.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
public class ElgamalDSA
{

    BigInteger x,q,a,y;

    MessageDigest md;


    public ElgamalDSA(int x,int q,int a) 
    {

        try
        {
            this.x=new BigInteger(String.valueOf(x));
            this.q=new BigInteger(String.valueOf(q));
            this.a=new BigInteger(String.valueOf(a));
            md=MessageDigest.getInstance("SHA-512");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    } 

    public String encryptAES(String message, String key) throws Exception 
    {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
     return Base64.getEncoder().encodeToString(cipherText);
    }


    public String decryptAES(String cipherText, String key) throws Exception {
    try {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        

        String paddedCipherText = cipherText;
        int padding = 4 - (cipherText.length() % 4);
        if (padding != 4) {
            paddedCipherText = cipherText + "=".repeat(padding);
        }
        
        byte[] decodedBytes = Base64.getDecoder().decode(paddedCipherText);
        
        if (decodedBytes.length % 16 != 0) {
            throw new IllegalBlockSizeException("Invalid encrypted data: length " + decodedBytes.length + " is not a multiple of 16");
        }
        
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("Decryption failed - possible causes: wrong key used or corrupted encrypted data", e);
        }
    }

    public BigInteger powerMod(BigInteger m,BigInteger n,BigInteger N)
    {
        BigInteger f=BigInteger.ONE;
        String b=n.toString(2);

        for(int i=0;i<b.length();i++)
        {
            f=(f.multiply(f)).mod(N);
            if(b.charAt(i)=='1')
            {
                f=(f.multiply(m)).mod(N);
            }
        }

        return f;
    }


    BigInteger phi(BigInteger n)
    {
        BigInteger result=n;

        for(BigInteger p=new BigInteger("2");p.multiply(p).compareTo(n)<0;p=p.add(BigInteger.ONE))
        {
            if(n.mod(p).equals(BigInteger.ZERO))
            {
                while(n.mod(p).equals(BigInteger.ZERO))
                    n=n.divide(p);
                result=result.subtract(result.divide(p));
            }
        }

        if(n.compareTo(BigInteger.ONE)>0)
            result=result.subtract(result.divide(n));
        
        return result;
    }


    public BigInteger GetHash(String M)
    {
        BigInteger bigi;
        byte messageBytes[]=M.getBytes(StandardCharsets.UTF_8);
        byte hash[]=md.digest(messageBytes);
        bigi=new BigInteger(1,hash);
        return bigi;
    }

    public int gcd(int a, int b)
    {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }


    BigInteger generateRandomNumberK() 
    {
    BigInteger Q_1 = q.subtract(BigInteger.ONE);
    SecureRandom rand = new SecureRandom();
    BigInteger k;
    do {
        k = new BigInteger(Q_1.bitLength(), rand);
    } while (k.compareTo(BigInteger.ONE) <= 0 || 
             k.compareTo(Q_1) >= 0 || 
             !k.gcd(Q_1).equals(BigInteger.ONE));
    return k;
}



    public String sign(String m)
    {
        String parts[]=m.split("\\s+");
        String M=parts[0];
        String ans="";
        String key=parts[1];
        try
        {
    
            String cipherText=encryptAES(M, key);
            BigInteger hash=GetHash(cipherText);
            System.out.println("Hash: "+hash);
            BigInteger K=generateRandomNumberK();
            System.out.println("Random number : "+K);
            
            y=a.modPow(x, q);
            BigInteger S1 = a.modPow(K, q);
            BigInteger powQ=phi(q.subtract(BigInteger.ONE)).subtract(BigInteger.ONE);
            BigInteger Q_1=q.subtract(BigInteger.ONE);
            BigInteger KInverse=K.modInverse(Q_1);
            BigInteger S2=(KInverse.multiply(hash.subtract(x.multiply(S1)))).mod(Q_1);
            if (S2.signum() < 0) S2 = S2.add(Q_1);
            ans=cipherText+" "+S1.toString()+" "+S2.toString()+" "+y.toString();
    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ans;
    }

    public String verify(String message)
    {

        String parts[]=message.split("\\s+");
        BigInteger hash=GetHash(parts[0]);
        BigInteger S1=new BigInteger(parts[1]);
        BigInteger S2=new BigInteger(parts[2]);
        BigInteger y=new BigInteger(parts[3]);
        String result="";
        try
        {
            String key=parts[4];54
            
            BigInteger V1=a.modPow(hash, q);
            BigInteger V2=(y.modPow(S1, q).multiply(S1.modPow(S2,q))).mod(q);
            result="";
            if(V1.equals(V2))
            {
                result=("The Signature is verified");
                try {
                    String decryptedMsg = decryptAES(parts[0], key);
                    result += " | Decrypted message: " + decryptedMsg;
                } catch (Exception e) {
                    result += " | Note: Cannot decrypt - key may not match the original encryption key";
                }
            }
            else
            {
                result=("The signature is not verified");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }



}