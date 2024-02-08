import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class OnlineShop {
    private Connection  connection;
    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<Order> orderHistory;

    public OnlineShop() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SimpleDB",
                    "postgres", "12345678");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showProductList() {
        System.out.println("Product List:");
        for (Product product : products) {
            System.out.println(product.getName() + " - $" + product.getCost() + " - Quantity: " + product.getQuantity());
        }
    }

    public void addProduct(String name, double cost, int quantity, String description) {
        Product product = new Product(name, cost, quantity, description);
        products.add(product);
        System.out.println("Product added successfully!");
    }

    public void addUser(int id, String name, double balance) {
        User user = new User(id, name, balance);
        users.add(user);
        System.out.println("User added successfully!");
    }

    public void buyProduct(int userId, String productName, int quantity) {
        User user = findUserById(userId);
        Product product = findProductByName(productName);

        if (user != null && product != null) {
            double totalCost = product.getCost() * quantity;
            if (user.getBalance() >= totalCost && product.getQuantity() >= quantity) {
                user.setBalance(user.getBalance() - totalCost);
                product.setQuantity(product.getQuantity() - quantity);

                Order order = new Order(userId, productName, quantity, totalCost);
                user.getOrders().add(order);
                orderHistory.add(order);

                System.out.println("Purchase successful!");
            } else {
                System.out.println("Insufficient balance or product quantity!");
            }
        } else {
            System.out.println("User or product not found!");
        }
    }

    public void returnProduct(int userId, String productName, int quantity) {
        User user = findUserById(userId);
        Product product = findProductByName(productName);

        if (user != null && product != null) {
            Order orderToRemove = findOrderToRemove(user, productName, quantity);

            if (orderToRemove != null) {
                user.setBalance(user.getBalance() + orderToRemove.getTotalSum());
                product.setQuantity(product.getQuantity() + quantity);

                user.getOrders().remove(orderToRemove);
                orderHistory.remove(orderToRemove);

                System.out.println("Return successful!");
            } else {
                System.out.println("Order not found for return!");
            }
        } else {
            System.out.println("User or product not found!");
        }
    }

    public void showAllUsers() {
        System.out.println("User List:");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " - Name: " + user.getName() + " - Balance: $" + user.getBalance());
        }
    }

    public void showUserOrders(int userId) {
        User user = findUserById(userId);
        if (user != null) {
            System.out.println("Orders for User " + user.getName() + ":");
            for (Order order : user.getOrders()) {
                System.out.println("Product: " + order.getProductName() + " - Quantity: " + order.getQuantity() + " - Total: $" + order.getTotalSum());
            }
        } else {
            System.out.println("User not found!");
        }
    }

    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    private Product findProductByName(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    private Order findOrderToRemove(User user, String productName, int quantity) {
        for (Order order : user.getOrders()) {
            if (order.getProductName().equals(productName) && order.getQuantity() == quantity) {
                return order;
            }
        }
        return null;
    }
}

