import java.io.*;
import java.net.*;
import java.util.*;

class RowColumn
{
    static String key = "HACK";

    static int[] getKeyOrder()
    {
        char[] sortedKey = key.toCharArray();
        char[] originalKey = key.toCharArray();
        Arrays.sort(sortedKey);

        int[] order = new int[key.length()];
        for (int i = 0; i < key.length(); i++)
        {
            for (int j = 0; j < sortedKey.length; j++)
            {
                if (originalKey[i] == sortedKey[j])
                {
                    order[i] = j;
                    sortedKey[j] = '\0'; // Mark as used
                    break;
                }
            }
        }
        return order;
    }

    public String encrypt(String plaintext)
    {
        plaintext = plaintext.toUpperCase().replaceAll("\\s", "");
        int rows = (int) Math.ceil((double) plaintext.length() / key.length());
        char[][] matrix = new char[rows][key.length()];

        // Fill matrix with plaintext
        int index = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < key.length(); j++)
            {
                if (index < plaintext.length())
                    matrix[i][j] = plaintext.charAt(index++);
                else
                    matrix[i][j] = 'X'; // Padding
            }
        }

        // Read columns in key order
        int[] order = getKeyOrder();
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < key.length(); i++)
        {
            for (int j = 0; j < key.length(); j++)
            {
                if (order[j] == i)
                {
                    for (int k = 0; k < rows; k++)
                        cipher.append(matrix[k][j]);
                    break;
                }
            }
        }
        return cipher.toString();
    }

    public String decrypt(String ciphertext)
    {
        ciphertext = ciphertext.toUpperCase().replaceAll("\\s", "");
        int rows = (int) Math.ceil((double) ciphertext.length() / key.length());
        char[][] matrix = new char[rows][key.length()];

        int[] order = getKeyOrder();
        int index = 0;

        // Fill columns in key order
        for (int i = 0; i < key.length(); i++)
        {
            for (int j = 0; j < key.length(); j++)
            {
                if (order[j] == i)
                {
                    for (int k = 0; k < rows; k++)
                    {
                        if (index < ciphertext.length())
                            matrix[k][j] = ciphertext.charAt(index++);
                    }
                    break;
                }
            }
        }

        // Read row-wise
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < key.length(); j++)
                plaintext.append(matrix[i][j]);
        }
        return plaintext.toString();
    }
}

public class ServerRowColumn
{
    private static void communicate(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            RowColumn rowColumn = new RowColumn();

            String hold = "";
            while (true)
            {
                hold = in.readUTF();
                System.out.println("Client : " + hold);

                hold = "Message received -> " + rowColumn.decrypt(hold);
                out.writeUTF(hold);
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
        int pn = 12000;

        System.out.println("Listening on port " + pn);
        try
        {
            ServerSocket server = new ServerSocket(pn);
            Socket socket = server.accept();
            System.out.println("Device connected");
            communicate(socket);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
