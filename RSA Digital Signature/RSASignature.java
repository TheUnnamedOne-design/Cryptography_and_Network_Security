import java.math.*;
import java.security.*;

public class RSASignature {

    private BigInteger n, e, d;
    private int keySize;

    public RSASignature(int keySize) {
        this.keySize = keySize;
        generateKeys();
    }

    private void generateKeys() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(keySize / 2, random);
        BigInteger q = BigInteger.probablePrime(keySize / 2, random);
        n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = new BigInteger("65537");
        d = e.modInverse(phi);
    }

    public static String bigIntegerToHex(BigInteger value) {
        return value.toString(16).toUpperCase();
    }

    public static BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    public String getPublicKey() {
        return bigIntegerToHex(n) + " " + bigIntegerToHex(e);
    }

    public String getPrivateKey() {
        return bigIntegerToHex(n) + " " + bigIntegerToHex(d);
    }

    public String sign(String message) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(message.getBytes());
        BigInteger hashValue = new BigInteger(1, hash);
        BigInteger signature = hashValue.modPow(d, n);
        
        return bigIntegerToHex(signature);
    }

    public boolean verify(String signatureHex, String message, String publicKeyStr) throws Exception {
        String[] keyParts = publicKeyStr.split("\\s+");
        BigInteger nVerify = hexToBigInteger(keyParts[0]);
        BigInteger eVerify = hexToBigInteger(keyParts[1]);

        BigInteger signature = hexToBigInteger(signatureHex);
        BigInteger decrypted = signature.modPow(eVerify, nVerify);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(message.getBytes());
        BigInteger hashValue = new BigInteger(1, hash);

        return decrypted.equals(hashValue);
    }
}
