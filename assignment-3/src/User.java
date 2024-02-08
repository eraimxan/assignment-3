import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private double balance;
    private ArrayList<Order> orders;

    public User(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.orders = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

