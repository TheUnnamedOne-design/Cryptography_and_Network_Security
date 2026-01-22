import java.util.Arrays;

public class IDEAEncryption {

    private int[] encryptionKeys;

    private int MUL(int a, int b) {
        a &= 0xFFFF;
        b &= 0xFFFF;
        long res = (long) a * b;
        if (res != 0) {
            int rem = (int) (res % 0x10001);
            return rem & 0xFFFF;
        } else {
            return (1 - a - b) & 0xFFFF;
        }
    }

    private int ADD(int a, int b) {
        return (a + b) & 0xFFFF;
    }

    private int XOR(int a, int b) {
        return (a ^ b) & 0xFFFF;
    }

    private int addInverse(int x) {
        return (0x10000 - x) & 0xFFFF;
    }

    private int mulInverse(int x) {
        if (x <= 1) return x;
        int m = 0x10001;
        int t0 = 0, t1 = 1;
        int r0 = m, r1 = x;
        while (r1 > 0) {
            int q = r0 / r1;
            int tempT = t1;
            t1 = t0 - q * t1;
            t0 = tempT;
            int tempR = r1;
            r1 = r0 - q * r1;
            r0 = tempR;
        }
        if (t0 < 0) t0 += m;
        return t0 & 0xFFFF;
    }

    private int[] generateEncryptionKeysProper(byte[] key) {
        int[] keys = new int[52];
        byte[] currentKey = Arrays.copyOf(key, 16);
        int count = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                if (count < 52) {
                    keys[count++] = ((currentKey[2 * j] & 0xFF) << 8) | (currentKey[2 * j + 1] & 0xFF);
                }
            }
            currentKey = shiftLeft25(currentKey);
        }
        for (int j = 0; j < 4; j++) {
            if (count < 52) {
                keys[count++] = ((currentKey[2 * j] & 0xFF) << 8) | (currentKey[2 * j + 1] & 0xFF);
            }
        }
        return keys;
    }

    private byte[] shiftLeft25(byte[] key) {
        byte[] shifted = new byte[16];
        int bitShift = 25;
        int byteShift = bitShift / 8;
        int remShift = bitShift % 8;

        for (int i = 0; i < 16; i++) {
            int current = (key[(i + byteShift) % 16] & 0xFF) << remShift;
            int next = (key[(i + byteShift + 1) % 16] & 0xFF) >>> (8 - remShift);
            shifted[i] = (byte) (current | next);
        }
        return shifted;
    }

    private int[] generateDecryptionKeys(int[] ek) {
        int[] dk = new int[52];
        dk[0] = mulInverse(ek[48]);
        dk[1] = addInverse(ek[49]);
        dk[2] = addInverse(ek[50]);
        dk[3] = mulInverse(ek[51]);
        dk[4] = ek[46];
        dk[5] = ek[47];

        for (int r = 1; r < 8; r++) {
            int dBase = r * 6;
            int eBase = (8 - r) * 6;
            dk[dBase + 0] = mulInverse(ek[eBase]);
            dk[dBase + 1] = addInverse(ek[eBase + 2]);
            dk[dBase + 2] = addInverse(ek[eBase + 1]);
            dk[dBase + 3] = mulInverse(ek[eBase + 3]);
            dk[dBase + 4] = ek[eBase - 2];
            dk[dBase + 5] = ek[eBase - 1];
        }

        dk[48] = mulInverse(ek[0]);
        dk[49] = addInverse(ek[1]);
        dk[50] = addInverse(ek[2]);
        dk[51] = mulInverse(ek[3]);
        return dk;
    }

    public byte[] processBlock(byte[] data, int[] keys) {
        int x1 = ((data[0] & 0xFF) << 8) | (data[1] & 0xFF);
        int x2 = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
        int x3 = ((data[4] & 0xFF) << 8) | (data[5] & 0xFF);
        int x4 = ((data[6] & 0xFF) << 8) | (data[7] & 0xFF);

        for (int round = 0; round < 8; round++) {
            int k = round * 6;
            int step1 = MUL(x1, keys[k]);
            int step2 = ADD(x2, keys[k + 1]);
            int step3 = ADD(x3, keys[k + 2]);
            int step4 = MUL(x4, keys[k + 3]);
            int step5 = XOR(step1, step3);
            int step6 = XOR(step2, step4);
            int step7 = MUL(step5, keys[k + 4]);
            int step8 = ADD(step6, step7);
            int step9 = MUL(step8, keys[k + 5]);
            int step10 = ADD(step7, step9);

            x1 = XOR(step1, step9);
            x4 = XOR(step4, step10);
            int t2 = XOR(step3, step9);
            int t3 = XOR(step2, step10);

            if (round < 7) {
                x2 = t2;
                x3 = t3;
            } else {
                x2 = t3;
                x3 = t2;
            }
        }

        int res1 = MUL(x1, keys[48]);
        int res2 = ADD(x2, keys[49]);
        int res3 = ADD(x3, keys[50]);
        int res4 = MUL(x4, keys[51]);

        return new byte[] {
            (byte)(res1 >> 8), (byte)res1,
            (byte)(res2 >> 8), (byte)res2,
            (byte)(res3 >> 8), (byte)res3,
            (byte)(res4 >> 8), (byte)res4
        };
    }

    public String encrypt(String plaintext, String key) {
        if (key.length() < 16) key = String.format("%-16s", key).replace(" ", "x");
        byte[] keyBytes = key.substring(0, 16).getBytes();
        int[] eKeys = generateEncryptionKeysProper(keyBytes);

        StringBuilder sb = new StringBuilder(plaintext);
        while (sb.length() % 8 != 0) sb.append(" ");
        byte[] ptBytes = sb.toString().getBytes();
        byte[] ctBytes = new byte[ptBytes.length];

        for (int i = 0; i < ptBytes.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(ptBytes, i, block, 0, 8);
            System.arraycopy(processBlock(block, eKeys), 0, ctBytes, i, 8);
        }
        try { return new String(ctBytes, "ISO-8859-1"); } catch (Exception e) { return new String(ctBytes); }
    }

    public String decrypt(String ciphertext, String key) {
        if (key.length() < 16) key = String.format("%-16s", key).replace(" ", "x");
        byte[] keyBytes = key.substring(0, 16).getBytes();
        int[] dKeys = generateDecryptionKeys(generateEncryptionKeysProper(keyBytes));

        byte[] ctBytes;
        try { ctBytes = ciphertext.getBytes("ISO-8859-1"); } catch (Exception e) { ctBytes = ciphertext.getBytes(); }
        byte[] ptBytes = new byte[ctBytes.length];

        for (int i = 0; i < ctBytes.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(ctBytes, i, block, 0, 8);
            System.arraycopy(processBlock(block, dKeys), 0, ptBytes, i, 8);
        }
        return new String(ptBytes).trim();
    }
}
