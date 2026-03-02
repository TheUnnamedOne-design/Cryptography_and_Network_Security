public class DiffieHellman
{
    int p;  // Prime modulus
    int g;  // Generator
    int privateKey;
    int publicKey;

    // Constructor for initializing with prime and generator
    public DiffieHellman(int p, int g)
    {
        this.p = p;
        this.g = g;
    }

    // Fast exponentiation: (base^exp) mod p
    int fastExp(int base, int exp, int mod)
    {
        String bin = Integer.toBinaryString(exp);
        int result = 1;
        for(int i = 0; i < bin.length(); i++)
        {
            result = (result * result) % mod;
            if(bin.charAt(i) == '1')
            {
                result = (result * base) % mod;
            }
        }
        return result;
    }

    // Generate public key from private key
    // Public Key = (g^privateKey) mod p
    int generatePublicKey(int privateKey)
    {
        this.privateKey = privateKey;
        this.publicKey = fastExp(g, privateKey, p);
        return this.publicKey;
    }

    // Compute shared secret from received public key
    // Shared Secret = (receivedPublicKey^privateKey) mod p
    int computeSharedSecret(int receivedPublicKey)
    {
        return fastExp(receivedPublicKey, privateKey, p);
    }

    // Get current public key
    int getPublicKey()
    {
        return this.publicKey;
    }

    // Get private key
    int getPrivateKey()
    {
        return this.privateKey;
    }
}
