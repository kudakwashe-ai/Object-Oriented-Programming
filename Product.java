public class Product {
    private String productId;
    private String productName;
    private double price;
    private int stock;
    private String category;

    public Product(String productId, String productName, double price, int stock, String category) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Add stock
    public void addStock(int amount) {
        if (amount > 0) {
            this.stock += amount;
            System.out.println(amount + " items added to stock. New stock for " + this.productName + ": " + this.stock);
        } else {
            System.out.println("Amount to add must be positive.");
        }
    }

    // Sell product (reduce stock)
    public void sellProduct(int amount) {
        if (amount > 0 && this.stock >= amount) {
            this.stock -= amount;
            System.out.println(amount + " " + this.productName + "(s) sold. Remaining stock: " + this.stock);
        } else {
            System.out.println("Failed to sell " + amount + " " + this.productName + "(s). Not enough stock or invalid amount.");
        }
    }

    // Display product details
    public void displayDetails() {
        System.out.println("Product ID: " + this.productId);
        System.out.println("Name: " + this.productName);
        System.out.println("Category: " + this.category);
        System.out.println("Price: $" + String.format("%.2f", this.price));
        System.out.println("Stock: " + this.stock);
    }

    // Apply discount
    public void applyDiscount(double percentage) {
        if (percentage > 0 && percentage <= 100) {
            double discountAmount = this.price * (percentage / 100);
            this.price -= discountAmount;
            System.out.println("Discount of " + percentage + "% applied to " + this.productName + ". New price: $" + String.format("%.2f", this.price));
        } else {
            System.out.println("Invalid discount percentage.");
        }
    }

    // Check stock availability
    public boolean checkAvailability(int amount) {
        boolean available = this.stock >= amount;
        System.out.println("Checking availability for " + amount + " " + this.productName + "(s): " + (available ? "In Stock" : "Out of Stock"));
        return available;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
