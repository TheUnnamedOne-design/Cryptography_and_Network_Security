public class TestDecrypt {
    public static void main(String[] args) {
        AES aes = new AES();
        
        // Test encryption and decryption
        String plaintext = "hello";
        String key = "mysecretpassword";
        
        // Encrypt
        String encrypted = aes.encrypt(plaintext + " " + key);
        System.out.println("Encrypted: " + encrypted);
        
        // Decrypt - need to pass hex key
        String keyHex = aes.textToHex(key);
        String decrypted = aes.decrypt(encrypted + " " + keyHex);
        System.out.println("Decrypted: " + decrypted);
        
        // Convert back to text
        String decryptedText = hexToText(decrypted);
        System.out.println("Decrypted text: " + decryptedText);
    }
    
    static String hexToText(String hex) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            text.append((char) Integer.parseInt(str, 16));
        }
        return text.toString();
    }
}
