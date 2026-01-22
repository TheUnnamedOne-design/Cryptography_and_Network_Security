public class Testing
{
    public static void main(String argsp[])
    {
        IDEAEncryption idea=new IDEAEncryption();

        String plaintext = "12345678";
        String key = "01234567";
        
        System.out.println("=== IDEA Encryption/Decryption Test ===");
        System.out.println("Original Plaintext: " + plaintext);
        System.out.println("Key: " + key);
        System.out.println();
        
        // Encrypt
        String encrypted = idea.encrypt(plaintext, key);
        System.out.println();
        
        // Decrypt
        String decrypted = idea.decrypt(encrypted, key);
        System.out.println();
        
        // Verify
        System.out.println("=== Verification ===");
        System.out.println("Original:  " + plaintext);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Match: " + plaintext.equals(decrypted));
    }
}