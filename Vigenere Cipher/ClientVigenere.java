import java.io.*;
import java.net.*;
import java.util.*;

class Vigenere
{
    static String key = "SECRET";

    static String generateKey(String str, String key)
    {
        int x = str.length();
        StringBuilder newKey = new StringBuilder(key);

        for (int i = 0; ; i++)
        {
            if (x == i)
                i = 0;
            if (newKey.length() == str.length())
                break;
            newKey.append(key.charAt(i));
        }
        return newKey.toString();
    }

    public String encrypt(String plaintext)
    {
        plaintext = plaintext.toUpperCase().replaceAll("\\s", "");
        String cipher = "";
        String generatedKey = generateKey(plaintext, key);

        for (int i = 0; i < plaintext.length(); i++)
        {
            int x = (plaintext.charAt(i) + generatedKey.charAt(i)) % 26;
            x += 'A';
            cipher += (char)(x);
        }
        return cipher;
    }

    public String decrypt(String ciphertext)
    {
        ciphertext = ciphertext.toUpperCase().replaceAll("\\s", "");
        String original = "";
        String generatedKey = generateKey(ciphertext, key);

        for (int i = 0; i < ciphertext.length(); i++)
        {
            int x = (ciphertext.charAt(i) - generatedKey.charAt(i) + 26) % 26;
            x += 'A';
            original += (char)(x);
        }
        return original;
    }
}

public class ClientVigenere
{
    private static void communicate(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            Vigenere vigenere = new Vigenere();

            String hold = "";
            while (true)
            {
                hold = br.readLine();
                hold = vigenere.encrypt(hold);
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
        int pn = 10000;

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
