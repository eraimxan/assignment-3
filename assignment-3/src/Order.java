public class Order {
    private int userId;
    private String productName;
    private int quantity;
    private double totalSum;

    public Order(int userId, String productName, int quantity, double totalSum) {
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalSum = totalSum;
    }

    public int getUserId() {
        return userId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalSum() {
        return totalSum;
    }
}

