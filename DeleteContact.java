import java.io.*;
import java.util.*;

public class DeleteContact {
    public static void execute(Scanner s) throws Exception {
        System.out.print("Keyword to delete: ");
        String k = s.nextLine().toLowerCase();
        File f = new File("phone.txt");
        if (!f.exists()) return;
        List<String> keep = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) 
                if (!l.toLowerCase().contains(k)) keep.add(l);
        }
        try (FileWriter w = new FileWriter(f)) {
            for (String l : keep) w.write(l + "\n");
        }
        System.out.println("Contact deleted if it existed.");
    }
}
