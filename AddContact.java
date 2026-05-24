import java.io.*;
import java.util.Scanner;

public class AddContact {
    public static void execute(Scanner s) throws Exception {
        System.out.print("Format (Name - Phone): ");
        String entry = s.nextLine();
        try (FileWriter w = new FileWriter("phone.txt", true)) {
            w.write(entry + "\n");
        }
        System.out.println("Contact added.");
    }
}
