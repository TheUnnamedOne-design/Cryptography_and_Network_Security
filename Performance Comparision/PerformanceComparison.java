import java.math.BigInteger;

public class PerformanceComparison {

    public static void main(String[] args) throws Exception {
        String message = "Hello this is the data";
        int iterations = 10;

        System.out.println("DSS vs RSA Performance Comparison\n");

        long dssKeyGen = System.nanoTime();
        DSS_DSA dss = new DSS_DSA(23, 11, 4);
        BigInteger x = dss.generatePrivateKey();
        BigInteger y = dss.generatePublicKey(x);
        String yHex = DSS_DSA.bigIntegerToHex(y);
        dssKeyGen = (System.nanoTime() - dssKeyGen) / 1_000_000;

        long dssSign = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            dss.sign(message, x);
        }
        dssSign = (System.nanoTime() - dssSign) / 1_000_000;

        long dssVerify = System.nanoTime();
        String sig = dss.sign(message, x);
        for (int i = 0; i < iterations; i++) {
            dss.verify(sig, message, yHex);
        }
        dssVerify = (System.nanoTime() - dssVerify) / 1_000_000;

        System.out.println("DSS:");
        System.out.println("  Key Generation: " + dssKeyGen + " ms");
        System.out.println("  Signing (10x): " + dssSign + " ms");
        System.out.println("  Verification (10x): " + dssVerify + " ms\n");

        long rsaKeyGen = System.nanoTime();
        RSASignature rsa = new RSASignature(2048);
        String publicKey = rsa.getPublicKey();
        rsaKeyGen = (System.nanoTime() - rsaKeyGen) / 1_000_000;

        long rsaSign = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            rsa.sign(message);
        }
        rsaSign = (System.nanoTime() - rsaSign) / 1_000_000;

        long rsaVerify = System.nanoTime();
        String rsaSig = rsa.sign(message);
        for (int i = 0; i < iterations; i++) {
            rsa.verify(rsaSig, message, publicKey);
        }
        rsaVerify = (System.nanoTime() - rsaVerify) / 1_000_000;

        System.out.println("RSA:");
        System.out.println("  Key Generation: " + rsaKeyGen + " ms");
        System.out.println("  Signing (10x): " + rsaSign + " ms");
        System.out.println("  Verification (10x): " + rsaVerify + " ms\n");

        System.out.println("Percentage Increase (RSA vs DSS):");
        double keyGenPercent = ((double)(rsaKeyGen - dssKeyGen) / dssKeyGen) * 100;
        double signPercent = ((double)(rsaSign - dssSign) / dssSign) * 100;
        double verifyPercent = ((double)(rsaVerify - dssVerify) / dssVerify) * 100;

        System.out.println("  Key Generation: " + String.format("%.2f", keyGenPercent) + "%");
        System.out.println("  Signing: " + String.format("%.2f", signPercent) + "%");
        System.out.println("  Verification: " + String.format("%.2f", verifyPercent) + "%");

        System.out.println("\nFaster Algorithm:");
        System.out.println("  Key Generation: " + (dssKeyGen < rsaKeyGen ? "DSS" : "RSA"));
        System.out.println("  Signing: " + (dssSign < rsaSign ? "DSS" : "RSA"));
        System.out.println("  Verification: " + (dssVerify < rsaVerify ? "DSS" : "RSA"));
    }
}
