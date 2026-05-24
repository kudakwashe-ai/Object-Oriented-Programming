import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        File f = new File("phone.txt");
        f.createNewFile(); // Ensure file exists
        while (true) {
            System.out.print("\n1.Add 2.View 3.Search 4.Delete 5.Exit -> ");
            String c = s.nextLine(), k, l;
            List<String> lines = new ArrayList<>();
            try (BufferedReader r = new BufferedReader(new FileReader(f))) { while ((l = r.readLine()) != null) lines.add(l); }
            if (c.equals("1")) {
                System.out.print("Format (Name - Phone): ");
                try (FileWriter w = new FileWriter(f, true)) { w.write(s.nextLine() + "\n"); }
            } else if (c.equals("2")) {
                lines.forEach(System.out::println);
            } else if (c.equals("3") || c.equals("4")) {
                System.out.print("Keyword: "); k = s.nextLine().toLowerCase();
                if (c.equals("3")) lines.stream().filter(x -> x.toLowerCase().contains(k)).forEach(System.out::println);
                else try (FileWriter w = new FileWriter(f)) { for (String x : lines) if (!x.toLowerCase().contains(k)) w.write(x + "\n"); }
            } else break;
        }
    }
}
