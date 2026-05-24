import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactManager {
    private static final String FILE_NAME = "phone.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Ensure file exists
        createFileIfNotExists();

        boolean running = true;
        while (running) {
            System.out.println("\n--- Contact Manager ---");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addContact();
                    break;
                case "2":
                    viewContacts();
                    break;
                case "3":
                    searchContact();
                    break;
                case "4":
                    deleteContact();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Creates the text file if it does not already exist
    private static void createFileIfNotExists() {
        try {
            File file = new File(FILE_NAME);
            if (file.createNewFile()) {
                System.out.println("Created new file: " + FILE_NAME);
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    // Adds a new contact to the text file
    private static void addContact() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Name and phone number cannot be empty.");
            return;
        }

        // Check for duplicate phone number
        if (isDuplicateNumber(phone)) {
            System.out.println("Error: Phone number already exists.");
            return;
        }

        // Append data to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + " - " + phone);
            writer.newLine();
            System.out.println("Contact added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Displays all saved contacts
    private static void viewContacts() {
        System.out.println("\n--- Saved Contacts ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean hasContacts = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasContacts = true;
            }
            if (!hasContacts) {
                System.out.println("No contacts found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Searches for a contact by name or phone number
    private static void searchContact() {
        System.out.print("Enter name or phone number to search: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        System.out.println("\n--- Search Results ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No matching contacts found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Deletes a contact from the file
    private static void deleteContact() {
        System.out.print("Enter the exact name or phone number of the contact to delete: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        List<String> remainingContacts = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // If the line contains the keyword, mark it as deleted and don't add to remainingContacts
                if (!deleted && line.toLowerCase().contains(keyword)) {
                    deleted = true;
                    System.out.println("Deleted contact: " + line);
                } else {
                    remainingContacts.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (deleted) {
            // Rewrite the file with remaining contacts
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (String contact : remainingContacts) {
                    writer.write(contact);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating file: " + e.getMessage());
            }
        } else {
            System.out.println("Contact not found.");
        }
    }

    // Helper method to prevent duplicate numbers
    private static boolean isDuplicateNumber(String phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Extracts the phone number part assuming format "Name - Phone"
                String[] parts = line.split(" - ");
                if (parts.length == 2 && parts[1].trim().equals(phone)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }
}
