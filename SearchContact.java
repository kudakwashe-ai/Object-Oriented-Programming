import java.io.*;
import java.util.Scanner;

public class SearchContact {
    public static void execute(Scanner s) throws Exception {
        System.out.print("Keyword: ");
        String k = s.nextLine().toLowerCase();
        File f = new File("phone.txt");
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) 
                if (l.toLowerCase().contains(k)) System.out.println(l);
        }
    }
}
