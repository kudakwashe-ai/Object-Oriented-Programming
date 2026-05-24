import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private static final String FILE_NAME = "phone.txt";

    public ContactManager() {
        createFileIfNotExists();
    }

    // Creates the text file if it does not already exist
    private void createFileIfNotExists() {
        try {
            File file = new File(FILE_NAME);
            if (file.createNewFile()) {
                System.out.println("Created new file: " + FILE_NAME);
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    // Checks for duplicate phone numbers in the file
    public boolean isDuplicateNumber(String phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
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

    // Adds a new contact to the text file
    public boolean addContact(Contact contact) {
        if (isDuplicateNumber(contact.getPhone())) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(contact.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    // Retrieves all saved contacts
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    contacts.add(new Contact(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return contacts;
    }

    // Searches for a contact by name or phone number
    public List<Contact> searchContact(String keyword) {
        List<Contact> foundContacts = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(lowerKeyword)) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 2) {
                        foundContacts.add(new Contact(parts[0].trim(), parts[1].trim()));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return foundContacts;
    }

    // Deletes a contact from the file matching the keyword
    public boolean deleteContact(String keyword) {
        List<String> remainingLines = new ArrayList<>();
        boolean deleted = false;
        String lowerKeyword = keyword.toLowerCase();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!deleted && line.toLowerCase().contains(lowerKeyword)) {
                    deleted = true; // Mark as deleted, do not add to remaining
                } else {
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        if (deleted) {
            // Rewrite the file with remaining contacts
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (String contactLine : remainingLines) {
                    writer.write(contactLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating file: " + e.getMessage());
                return false;
            }
        }
        return deleted;
    }
}
