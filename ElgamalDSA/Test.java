public class Test
{
    public static void main(String args[])
    {
        ElgamalDSA elgamal=new ElgamalDSA(16,19,10);
        // int m=67;
        // int n=782;
        // int N=901;
        // System.out.println(elgamal.powerMod(m, n, N));

        // System.out.println(elgamal.phi(6738));

        String M="HelloHi";
        String message=elgamal.sign(M);
        message="5"+message;

        System.out.print(elgamal.verify(message));
    }
}