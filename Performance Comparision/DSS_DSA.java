import java.math.*;
import java.security.*;

public class DSS_DSA {

    BigInteger p, q, g;

    public DSS_DSA(int p, int q, int g) {
        this.p = new BigInteger(String.valueOf(p));
        this.q = new BigInteger(String.valueOf(q));
        this.g = new BigInteger(String.valueOf(g));
    }

    public static String bigIntegerToHex(BigInteger value) {
        return value.toString(16).toUpperCase();
    }

    public static BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    public BigInteger generatePublicKey(BigInteger x) {
        return g.modPow(x, p);
    }

    public BigInteger generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        BigInteger x;
        do {
            x = new BigInteger(q.bitLength(), random);
        } while (x.compareTo(BigInteger.ONE) < 0 || x.compareTo(q) >= 0);
        return x;
    }

    public String sign(String message, BigInteger x) throws Exception {
        SecureRandom random = new SecureRandom();

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        BigInteger hash = new BigInteger(1, md.digest(message.getBytes())).mod(q);

        BigInteger k, r, s;

        do {
            do {
                k = new BigInteger(q.bitLength(), random);
            } while (k.compareTo(BigInteger.ONE) < 0 || k.compareTo(q) >= 0);

            r = g.modPow(k, p).mod(q);
            s = (k.modInverse(q).multiply(hash.add(x.multiply(r)))).mod(q);

        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));

        return bigIntegerToHex(r) + " " + bigIntegerToHex(s);
    }

    public boolean verify(String signParts, String message, String yHex) throws Exception {

        String parts[] = signParts.split("\\s+");

        BigInteger r = hexToBigInteger(parts[0]);
        BigInteger s = hexToBigInteger(parts[1]);
        BigInteger y = hexToBigInteger(yHex);

        if (r.compareTo(BigInteger.ONE) < 0 || r.compareTo(q) >= 0) return false;
        if (s.compareTo(BigInteger.ONE) < 0 || s.compareTo(q) >= 0) return false;

        BigInteger w = s.modInverse(q);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        BigInteger hash = new BigInteger(1, md.digest(message.getBytes())).mod(q);

        BigInteger u1 = hash.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);

        BigInteger v = (g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p)).mod(q);

        return v.equals(r);
    }
}