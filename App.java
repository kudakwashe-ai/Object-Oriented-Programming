import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print("\n1.Add 2.View 3.Search 4.Delete 5.Exit -> ");
            String c = s.nextLine();
            if (c.equals("1")) AddContact.execute(s);
            else if (c.equals("2")) ViewContacts.execute();
            else if (c.equals("3")) SearchContact.execute(s);
            else if (c.equals("4")) DeleteContact.execute(s);
            else break;
        }
    }
}
