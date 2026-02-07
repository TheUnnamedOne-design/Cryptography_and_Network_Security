import java.util.Random;

public class Elgamal
{
    
    static int q,alpha,Y_A,X_A;

    //Client Constructor
    public Elgamal(int q,int a,int Y_a)
    {
        this.q=q;
        this.alpha=a;
        this.Y_A=Y_a;
    }

    //Server Constructor
    public Elgamal(int q,int a,int X_a,boolean PrivateKey)
    {
        this.q=q;
        this.alpha=a;
        this.X_A=X_a;
    }

    int phi(int n)
    {
        int ans=n;
        for(int i=2;i*i<=n;i++)
        {
            if(n%i==0)
            {
                ans-=ans/i;
                while(n%i==0)
                {
                    n/=i;
                }
            }
        }
        if(n>1)
        {
            ans-=ans/n;
        }
        return ans;
    }


    int fast_exp(int n,int a,int b)
    {
        String bin=Integer.toBinaryString(b);
        int f=1;
        for(int i=0;i<bin.length();i++)
        {
            f=(f*f)%n;
            if(bin.charAt(i)=='1')
            {
                f=(f*a)%n;
            }
        }
        return f;
    }

    int[] encrypt(int M)
    {


        Random rand = new Random();
        int k = rand.nextInt(q - 1) + 1;

        int arr[]=new int[2];

        int K=fast_exp(q, Y_A, k);

        int c1=fast_exp(q, alpha, k);
        int c2=(K*M)%q;

        arr[0]=c1;
        arr[1]=c2;
        return arr;
    }


    int decrypt(int c1,int c2)
    {
        int K=fast_exp(q, c1, X_A);

        int K_inv=fast_exp(q, K, phi(q)-1);

        int M=(c2*K_inv)%q;

        return M;
    }
}