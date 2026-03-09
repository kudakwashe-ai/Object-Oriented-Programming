public class Main {
    public static void main(String[] args) {
        // Create a new product instance
        Product laptop = new Product("P001", "Gaming Laptop", 1200.00, 10, "Electronics");
        Product headphones = new Product("P002", "Wireless Headphones", 150.00, 50, "Accessories");
        
        System.out.println("--- Initial Product Details ---");
        laptop.displayDetails();
        System.out.println();
        headphones.displayDetails();
        
        System.out.println("\n--- Checking Stock Availability ---");
        laptop.checkAvailability(5);
        laptop.checkAvailability(15);
        
        System.out.println("\n--- Selling Product ---");
        laptop.sellProduct(3);
        headphones.sellProduct(60); // Should fail due to low stock
        
        System.out.println("\n--- Adding Stock ---");
        laptop.addStock(5);
        
        System.out.println("\n--- Applying Discount ---");
        laptop.applyDiscount(10); // 10% discount on laptop
        
        System.out.println("\n--- Final Product Details ---");
        laptop.displayDetails();
    }
}
