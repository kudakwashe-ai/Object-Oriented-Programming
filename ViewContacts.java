import java.io.*;

public class ViewContacts {
    public static void execute() throws Exception {
        File f = new File("phone.txt");
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) System.out.println(l);
        }
    }
}
