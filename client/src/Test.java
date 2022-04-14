public class Test {
    public static void main(String[] args) {
        String s="add&123,123,123";

        String[] split = s.split("&");
        for (String s1 : split) {
            System.out.println(s1);
        }
    }
}
