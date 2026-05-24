import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ContactManager manager = new ContactManager();

    public static void main(String[] args) {
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

    private static void addContact() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Name and phone number cannot be empty.");
            return;
        }

        Contact contact = new Contact(name, phone);
        if (manager.addContact(contact)) {
            System.out.println("Contact added successfully.");
        } else {
            System.out.println("Error: Phone number already exists or failed to save.");
        }
    }

    private static void viewContacts() {
        System.out.println("\n--- Saved Contacts ---");
        List<Contact> contacts = manager.getAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            for (Contact contact : contacts) {
                System.out.println(contact);
            }
        }
    }

    private static void searchContact() {
        System.out.print("Enter name or phone number to search: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        System.out.println("\n--- Search Results ---");
        List<Contact> results = manager.searchContact(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching contacts found.");
        } else {
            for (Contact contact : results) {
                System.out.println(contact);
            }
        }
    }

    private static void deleteContact() {
        System.out.print("Enter the exact name or phone number of the contact to delete: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        if (manager.deleteContact(keyword)) {
            System.out.println("Contact deleted successfully.");
        } else {
            System.out.println("Contact not found or deletion failed.");
        }
    }
}
