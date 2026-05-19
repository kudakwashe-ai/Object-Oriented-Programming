import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a sentence: ");
        if (!scanner.hasNextLine()) {
            scanner.close();
            return;
        }
        String input = scanner.nextLine();

        // Instantiate all operations polymorphically
        List<SentenceOperation> operations = new ArrayList<>();
        operations.add(new CharacterCounter());
        operations.add(new WordCounter());
        operations.add(new UppercaseConverter());
        operations.add(new LowercaseConverter());
        operations.add(new SentenceReverser());
        operations.add(new VowelCounter());
        operations.add(new ConsonantCounter());
        operations.add(new PalindromeChecker());

        System.out.println();
        // Execute and print each operation
        for (SentenceOperation op : operations) {
            System.out.println(op.getLabel() + ": " + op.execute(input));
        }

        scanner.close();
    }
}
