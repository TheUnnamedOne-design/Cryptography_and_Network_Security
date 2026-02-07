public class RSA
{
     int e,d,n;

    //Client Constructor
    public RSA(int n,int e)
    {
        this.n=n;
        this.e=e;
    }

    //Server Constructor
    public RSA(int n,int d,boolean PrivateKey)
    {
        this.d=d;
        this.n=n;
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

    int encrypt(int x)
    {
        int v=fast_exp(n, x, e);
        return v;
    }
    int decrypt(int x)
    {
        int v=fast_exp(n, x, d);

        return v;
    }
}