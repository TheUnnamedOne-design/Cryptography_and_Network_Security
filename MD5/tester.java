public class tester
{
    public static void main(String[] args) {
        MD5 obj=new MD5();

        String message="abc";

        System.out.println(obj.hash(message));

    }
}