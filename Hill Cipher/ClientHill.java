import java.io.*;
import java.net.*;

class Hill
{
    static int[][] keyMatrix = {{6, 24, 1}, {13, 16, 10}, {20, 17, 15}};
    static int n = 3; // Matrix size

    static int mod(int a, int m)
    {
        return (a % m + m) % m;
    }

    static int[][] getInverseMatrix()
    {
        // For demonstration, using a pre-computed inverse
        int[][] inv = {{8, 5, 10}, {21, 8, 21}, {21, 12, 8}};
        return inv;
    }

    public String encrypt(String plaintext)
    {
        plaintext = plaintext.toUpperCase().replaceAll("\\s", "");
        StringBuilder ciphertext = new StringBuilder();

        // Pad if necessary
        while (plaintext.length() % n != 0)
            plaintext += 'X';

        for (int i = 0; i < plaintext.length(); i += n)
        {
            int[] block = new int[n];
            for (int j = 0; j < n; j++)
                block[j] = plaintext.charAt(i + j) - 'A';

            int[] result = new int[n];
            for (int j = 0; j < n; j++)
            {
                result[j] = 0;
                for (int k = 0; k < n; k++)
                    result[j] += keyMatrix[j][k] * block[k];
                result[j] = mod(result[j], 26);
                ciphertext.append((char) (result[j] + 'A'));
            }
        }
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext)
    {
        ciphertext = ciphertext.toUpperCase().replaceAll("\\s", "");
        StringBuilder plaintext = new StringBuilder();
        int[][] invMatrix = getInverseMatrix();

        for (int i = 0; i < ciphertext.length(); i += n)
        {
            int[] block = new int[n];
            for (int j = 0; j < n; j++)
                block[j] = ciphertext.charAt(i + j) - 'A';

            int[] result = new int[n];
            for (int j = 0; j < n; j++)
            {
                result[j] = 0;
                for (int k = 0; k < n; k++)
                    result[j] += invMatrix[j][k] * block[k];
                result[j] = mod(result[j], 26);
                plaintext.append((char) (result[j] + 'A'));
            }
        }
        return plaintext.toString();
    }
}

public class ClientHill
{
    private static void communicate(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            Hill hill = new Hill();

            String hold = "";
            while (true)
            {
                hold = br.readLine();
                hold = hill.encrypt(hold);
                out.writeUTF(hold);
                System.out.println("Client : " + hold);

                hold = in.readUTF();
                System.out.println("Server : " + hold);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        int pn = 9000;

        System.out.println("Connecting to server on port " + pn);
        try
        {
            Socket socket = new Socket("127.0.0.1", pn);
            System.out.println("Connected to server");
            communicate(socket);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
