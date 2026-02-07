
import java.util.ArrayList;

public class Testing {
    public static void main(String args[])
    {
        SDESEncryption obj=new SDESEncryption();
        ArrayList<String> keys = new ArrayList<>();
        obj.key_generate("1011111011");
    }
}
