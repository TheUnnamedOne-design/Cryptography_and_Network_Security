
import java.math.BigInteger;


public class PrimeGenerate
{
    static boolean isPrime(BigInteger n)
    {
        BigInteger i=new BigInteger("2");
        while((i.multiply(i)).compareTo(n)<=0)
        {
            if((n.mod(i)).equals(BigInteger.ZERO)) return false;
            i=i.add(BigInteger.ONE);
        }
        return true;
    }
    public static void main(String argsp[])
    {
        int n=10;

        int exp=32;
        BigInteger base=new BigInteger(Integer.valueOf(n).toString());
        BigInteger start=base.pow(exp);
        BigInteger end=base.pow(exp+1);


        long startTime = System.currentTimeMillis();


        BigInteger i=start;
        while(!i.equals(end))
        {
            if(isPrime(i))
            {
                System.out.println(i);
                break;
            }
            if(i.equals(end.subtract(BigInteger.ONE)))
            {
                System.out.println("All checked");
            }
            i=i.add(BigInteger.ONE);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Elapsed Time: " + (endTime - startTime) + " milliseconds");
    }
}