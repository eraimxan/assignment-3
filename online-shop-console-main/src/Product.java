public class Product {
    private String name;
    private double cost;
    private int quantity;
    private String description;

    public Product(String name, double cost, int quantity, String description) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.description = description;
    }

    public String getName() {
        
        return name;
    }

    public double getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

