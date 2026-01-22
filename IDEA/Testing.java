public class Testing
{
    public static void main(String argsp[])
    {
        IDEAEncryption idea=new IDEAEncryption();

        idea.encrypt("12345678", "01234567");
    }
}