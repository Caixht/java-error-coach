public class Crash4 {
    public static void main(String[] args) throws Exception {
        Class.forName("com.example.NotExist");
    }
}
