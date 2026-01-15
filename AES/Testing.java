public class Testing {
    public static void main(String args[])
    {
        AES obj=new AES();
        String holder[]={"77","6f","72","64"};
        String word[]=obj.f_function(holder, 1);

        int i;
        for(i=0;i<4;i++)
        {
            System.out.print(word[i]+" ");
        }
    }
}
