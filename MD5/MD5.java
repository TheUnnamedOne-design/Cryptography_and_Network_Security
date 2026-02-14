import java.nio.charset.StandardCharsets;

public class MD5 {

    int T[] = new int[64];

    static final int[] shift = {
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };

    public MD5() {
        for (int i = 0; i < 64; i++) {
            T[i] = (int) (long) (Math.abs(Math.sin(i + 1)) * (1L << 32));
        }
    }

    byte[] toLittleEndian(int value) {
        byte[] out = new byte[4];

        out[0] = (byte) (value);
        out[1] = (byte) (value >>> 8);
        out[2] = (byte) (value >>> 16);
        out[3] = (byte) (value >>> 24);

        return out;
    }

    public int F(int B, int C, int D) {
        return (B & C) | (~B & D);
    }

    public int G(int B, int C, int D) {
        return (B & D) | (C & ~D);
    }

    public int H(int B, int C, int D) {
        return B ^ C ^ D;
    }

    public int I(int B, int C, int D) {
        return C ^ (B | ~D);
    }

    private byte[] padMessage(byte message[]) {
        int l = message.length;
        long bl = (long) l * 8;

        int paddinglength = (l % 64 < 56) ? (56 - l % 64) : (120 - l % 64);

        byte[] padded = new byte[l + paddinglength + 8];

        System.arraycopy(message, 0, padded, 0, l);

        padded[l] = (byte) 0x80;

        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (bl >>> (8 * i));
        }

        return padded;
    }

    public String hash(String m) {
        byte message[] = m.getBytes(StandardCharsets.UTF_8);
        byte[] padded = padMessage(message);

        int A = 0x67452301;
        int B = 0xefcdab89;
        int C = 0x98badcfe;
        int D = 0x10325476;

        int N = padded.length / 64;

        for (int q = 0; q < N; q++) {
            int AA = A;
            int BB = B;
            int CC = C;
            int DD = D;

            int X[] = new int[16];
            int start = q * 64;

            for (int j = 0; j < 16; j++) {
                int index = start + j * 4;
                X[j] = (padded[index] & 0xff) | 
                       ((padded[index + 1] & 0xff) << 8) | 
                       ((padded[index + 2] & 0xff) << 16) | 
                       ((padded[index + 3] & 0xff) << 24);
            }

            // Round 1
            for (int i = 0; i < 16; i++) {
                int temp = B + Integer.rotateLeft(A + F(B, C, D) + X[i] + T[i], shift[i]);
                A = D;
                D = C;
                C = B;
                B = temp;
            }

            // Round 2
            for (int i = 16; i < 32; i++) {
                int temp = B + Integer.rotateLeft(A + G(B, C, D) + X[(5 * i + 1) % 16] + T[i], shift[i]);
                A = D;
                D = C;
                C = B;
                B = temp;
            }

            // Round 3
            for (int i = 32; i < 48; i++) {
                int temp = B + Integer.rotateLeft(A + H(B, C, D) + X[(3 * i + 5) % 16] + T[i], shift[i]);
                A = D;
                D = C;
                C = B;
                B = temp;
            }

            // Round 4
            for (int i = 48; i < 64; i++) {
                int temp = B + Integer.rotateLeft(A + I(B, C, D) + X[(7 * i) % 16] + T[i], shift[i]);
                A = D;
                D = C;
                C = B;
                B = temp;
            }

            A = A + AA;
            B = B + BB;
            C = C + CC;
            D = D + DD;
        }

        StringBuilder sb = new StringBuilder();
        int[] res = {A, B, C, D};

        for (int val : res) {
            byte[] bytes = toLittleEndian(val);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
        }

        return sb.toString();
    }
}