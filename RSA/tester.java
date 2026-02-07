public class tester {

    public static void main(String[] args) {

        RSA obj=new RSA(17,11,187);
        int v=obj.encrypt(88, 7);
        System.out.println(v);
        int d=obj.encrypt(v, 23);
        System.out.println(d);

    }
    
}
