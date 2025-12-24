import java.io.*;
import java.net.*;
import java.util.*;

class RailFence
{
    static int numRails = 3;

    public String encrypt(String plaintext)
    {
        plaintext = plaintext.replaceAll("\\s", "");
        if (plaintext.length() <= numRails)
            return plaintext;

        char[][] rail = new char[numRails][plaintext.length()];
        for (int i = 0; i < numRails; i++)
            Arrays.fill(rail[i], '\n');

        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < plaintext.length(); i++)
        {
            if (row == 0 || row == numRails - 1)
                dirDown = !dirDown;

            rail[row][col++] = plaintext.charAt(i);

            if (dirDown)
                row++;
            else
                row--;
        }

        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < numRails; i++)
        {
            for (int j = 0; j < plaintext.length(); j++)
            {
                if (rail[i][j] != '\n')
                    cipher.append(rail[i][j]);
            }
        }
        return cipher.toString();
    }

    public String decrypt(String ciphertext)
    {
        ciphertext = ciphertext.replaceAll("\\s", "");
        if (ciphertext.length() <= numRails)
            return ciphertext;

        char[][] rail = new char[numRails][ciphertext.length()];
        for (int i = 0; i < numRails; i++)
            Arrays.fill(rail[i], '\n');

        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < ciphertext.length(); i++)
        {
            if (row == 0)
                dirDown = true;
            if (row == numRails - 1)
                dirDown = false;

            rail[row][col++] = '*';

            if (dirDown)
                row++;
            else
                row--;
        }

        int index = 0;
        for (int i = 0; i < numRails; i++)
        {
            for (int j = 0; j < ciphertext.length(); j++)
            {
                if (rail[i][j] == '*' && index < ciphertext.length())
                    rail[i][j] = ciphertext.charAt(index++);
            }
        }

        StringBuilder plaintext = new StringBuilder();
        row = 0;
        col = 0;
        for (int i = 0; i < ciphertext.length(); i++)
        {
            if (row == 0)
                dirDown = true;
            if (row == numRails - 1)
                dirDown = false;

            if (rail[row][col] != '*')
                plaintext.append(rail[row][col++]);

            if (dirDown)
                row++;
            else
                row--;
        }
        return plaintext.toString();
    }
}

public class ServerRailFence
{
    private static void communicate(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            RailFence railFence = new RailFence();

            String hold = "";
            while (true)
            {
                hold = in.readUTF();
                System.out.println("Client : " + hold);

                hold = "Message received -> " + railFence.decrypt(hold);
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
        int pn = 11000;

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
