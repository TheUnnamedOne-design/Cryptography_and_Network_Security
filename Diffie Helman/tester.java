import java.util.Scanner;

public class tester
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=== Diffie-Hellman Key Exchange Demo ===\n");
        
        // Get public parameters
        System.out.print("Enter prime number (p): ");
        int p = sc.nextInt();
        
        System.out.print("Enter generator (g): ");
        int g = sc.nextInt();
        
        // Create DiffieHellman instances for both parties
        DiffieHellman alice = new DiffieHellman(p, g);
        DiffieHellman bob = new DiffieHellman(p, g);
        
        // Alice's keys
        System.out.print("\nEnter Alice's private key: ");
        int alicePrivate = sc.nextInt();
        int alicePublic = alice.generatePublicKey(alicePrivate);
        System.out.println("Alice's Public Key: " + alicePublic);
        
        // Bob's keys
        System.out.print("\nEnter Bob's private key: ");
        int bobPrivate = sc.nextInt();
        int bobPublic = bob.generatePublicKey(bobPrivate);
        System.out.println("Bob's Public Key: " + bobPublic);
        
        // Exchange public keys and compute shared secrets
        System.out.println("\n--- Exchanging Public Keys ---");
        int aliceSharedSecret = alice.computeSharedSecret(bobPublic);
        int bobSharedSecret = bob.computeSharedSecret(alicePublic);
        
        System.out.println("\n=== Results ===");
        System.out.println("Alice's Shared Secret: " + aliceSharedSecret);
        System.out.println("Bob's Shared Secret: " + bobSharedSecret);
        
        if(aliceSharedSecret == bobSharedSecret)
        {
            System.out.println("\n✓ SUCCESS! Both parties have the same shared secret!");
            System.out.println("Shared Secret Key: " + aliceSharedSecret);
        }
        else
        {
            System.out.println("\n✗ FAILED! Shared secrets don't match!");
        }
        
        System.out.println("\n=== Verification ===");
        System.out.println("Alice computed: (" + bobPublic + "^" + alicePrivate + ") mod " + p + " = " + aliceSharedSecret);
        System.out.println("Bob computed: (" + alicePublic + "^" + bobPrivate + ") mod " + p + " = " + bobSharedSecret);
        
        sc.close();
    }
}
