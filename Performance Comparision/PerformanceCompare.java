import java.io.*;
import java.math.BigInteger;

public class PerformanceCompare { 

    public static void main(String[] args) throws Exception {
        String[] payloads = {"payload_1KB.txt", "payload_5KB.txt", "payload_25KB.txt", "payload_100KB.txt", "payload_1MB.txt"};

        System.out.println("Payload Signing Comparison\n");
        System.out.println(String.format("%-15s | %-15s | %-15s | %-10s", "Payload", "DSS Sign (ms)", "RSA Sign (ms)", "Faster"));

        for (String payload : payloads) {
            String content = readFile(payload);
            if (content == null) {
                System.out.println(String.format("%-15s | File not found", payload));
                continue;
            }

            long dssSign = System.nanoTime();
            DSS_DSA dss = new DSS_DSA(23, 11, 4);
            BigInteger x = dss.generatePrivateKey();
            dss.sign(content, x);
            dssSign = (System.nanoTime() - dssSign) / 1_000_000;

            long rsaSign = System.nanoTime();
            RSASignature rsa = new RSASignature(2048);
            rsa.sign(content);
            rsaSign = (System.nanoTime() - rsaSign) / 1_000_000;

            String faster = dssSign < rsaSign ? "DSS" : "RSA";
            System.out.println(String.format("%-15s | %-15d | %-15d | %-10s", payload, dssSign, rsaSign, faster));
        }

        System.out.println("\nPayload Verification Comparison\n");
        System.out.println(String.format("%-15s | %-15s | %-15s | %-10s", "Payload", "DSS Verify (ms)", "RSA Verify (ms)", "Faster"));

        for (String payload : payloads) {
            String content = readFile(payload);
            if (content == null) {
                System.out.println(String.format("%-10s | File not found", payload));
                continue;
            }

            DSS_DSA dss = new DSS_DSA(23, 11, 4);
            BigInteger x = dss.generatePrivateKey();
            BigInteger y = dss.generatePublicKey(x);
            String yHex = DSS_DSA.bigIntegerToHex(y);
            String dssSig = dss.sign(content, x);

            long dssVerify = System.nanoTime();
            dss.verify(dssSig, content, yHex);
            dssVerify = (System.nanoTime() - dssVerify) / 1_000_000;

            RSASignature rsa = new RSASignature(2048);
            String publicKey = rsa.getPublicKey();
            String rsaSig = rsa.sign(content);

            long rsaVerify = System.nanoTime();
            rsa.verify(rsaSig, content, publicKey);
            rsaVerify = (System.nanoTime() - rsaVerify) / 1_000_000;

            String faster = dssVerify < rsaVerify ? "DSS" : "RSA";
            System.out.println(String.format("%-10s | %-15d | %-15d | %-10s", payload, dssVerify, rsaVerify, faster));
        }
    }

    private static String readFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}

