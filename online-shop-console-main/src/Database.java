import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;

public class Database {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private  static final String username = "postgres";
    private static final String password = "12345678";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
class UserDatabase {
    private Connection connection;

    public UserDatabase() {
        try {
            connection = Database.connect();
        } catch (SQLException e) {
            // Handle the exception here, or rethrow it as a RuntimeException
            e.printStackTrace();
        }
    }

    public void addUser(int userId, String userName, double userBalance) {
        String query = "INSERT INTO UsersTable (id, name, balance) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, userName);
            statement.setDouble(3, userBalance);

            statement.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showAllUsers() {
        String sql = "SELECT * FROM UsersTable";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("User List:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                System.out.println("ID: " + id + " - Name: " + name + " - Balance: $" + balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserBalance(int id, double newBalance) {
        String sql = "UPDATE UsersTable SET balance = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User balance updated successfully!");
            } else {
                System.out.println("User not found or balance update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM UsersTable WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found or delete failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
class ProductDatabase {
    private Connection connection;

    public ProductDatabase() {
        try {
            connection = Database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        //Create operation
    public void addProduct(String name, double cost, int quantity, String description) throws SQLException {
        String insertProductSQL = "INSERT INTO ProductsTable (name, cost, quantity, description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertProductStmt = connection.prepareStatement(insertProductSQL)) {
            insertProductStmt.setString(1, name);
            insertProductStmt.setDouble(2, cost);
            insertProductStmt.setInt(3, quantity);
            insertProductStmt.setString(4, description);
            insertProductStmt.executeUpdate();

            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }


    // Read Operation
    public void showProductList() {
        String sql = "SELECT * FROM ProductsTable";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("Product List:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double cost = resultSet.getDouble("cost");
                int quantity = resultSet.getInt("quantity");
                System.out.println(name + " - $" + cost + " - Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Operation
    public void updateProductQuantity(String name, int newQuantity) {
        String sql = "UPDATE ProductsTable SET quantity = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newQuantity);
            statement.setString(2, name);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product quantity updated successfully!");
            } else {
                System.out.println("Product not found or quantity update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Operation
    public void deleteProduct(String productName) throws SQLException {
        String deleteProductSQL = "DELETE FROM ProductsTable WHERE name = ?";

        try (PreparedStatement deleteProductStmt = connection.prepareStatement(deleteProductSQL)) {
            deleteProductStmt.setString(1, productName);
            int rowsAffected = deleteProductStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("No product found with the specified name.");
            }
        }
    }




    public void buyProduct(int userId, String productName, int quantity) throws SQLException {
        String getProductSQL = "SELECT cost, quantity FROM ProductsTable WHERE name = ?";
        String insertOrderSQL = "INSERT INTO OrdersTable (user_id, product_name, quantity, total_sum) VALUES (?, ?, ?, ?)";
        String updateProductSQL = "UPDATE ProductsTable SET quantity = quantity - ? WHERE name = ?";
        String updateUserBalanceSQL = "UPDATE UsersTable SET balance = balance - ? WHERE id = ?";

        try (PreparedStatement getProductStmt = connection.prepareStatement(getProductSQL);
             PreparedStatement insertOrderStmt = connection.prepareStatement(insertOrderSQL);
             PreparedStatement updateProductStmt = connection.prepareStatement(updateProductSQL);
             PreparedStatement updateUserBalanceStmt = connection.prepareStatement(updateUserBalanceSQL)) {

            connection.setAutoCommit(false);

            // Get product details
            getProductStmt.setString(1, productName);
            ResultSet productResult = getProductStmt.executeQuery();

            if (productResult.next()) {
                double cost = productResult.getDouble("cost");
                int availableQuantity = productResult.getInt("quantity");

                if (availableQuantity >= quantity) {
                    double totalCost = cost * quantity;

                    // Update product quantity
                    updateProductStmt.setInt(1, quantity);
                    updateProductStmt.setString(2, productName);
                    updateProductStmt.executeUpdate();

                    // Update user balance
                    updateUserBalanceStmt.setDouble(1, totalCost);
                    updateUserBalanceStmt.setInt(2, userId);
                    updateUserBalanceStmt.executeUpdate();

                    // Insert order
                    insertOrderStmt.setInt(1, userId);
                    insertOrderStmt.setString(2, productName);
                    insertOrderStmt.setInt(3, quantity);
                    insertOrderStmt.setDouble(4, totalCost);
                    insertOrderStmt.executeUpdate();

                    System.out.println("Purchase successful!");
                    connection.commit();
                } else {
                    System.out.println("Insufficient quantity available!");
                    connection.rollback();
                }
            } else {
                System.out.println("Product not found!");
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void returnProduct(int userId, String productName, int quantity) throws SQLException {
        String getOrderSQL = "SELECT * FROM OrdersTable WHERE user_id = ? AND product_name = ? AND quantity = ?";
        String updateProductSQL = "UPDATE ProductsTable SET quantity = quantity + ? WHERE name = ?";
        String updateUserBalanceSQL = "UPDATE UsersTable SET balance = balance + ? WHERE id = ?";
        String deleteOrderSQL = "DELETE FROM OrdersTable WHERE user_id = ? AND product_name = ? AND quantity = ?";

        try (PreparedStatement getOrderStmt = connection.prepareStatement(getOrderSQL);
             PreparedStatement updateProductStmt = connection.prepareStatement(updateProductSQL);
             PreparedStatement updateUserBalanceStmt = connection.prepareStatement(updateUserBalanceSQL);
             PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderSQL)) {

            connection.setAutoCommit(false);

            // Get order details
            getOrderStmt.setInt(1, userId);
            getOrderStmt.setString(2, productName);
            getOrderStmt.setInt(3, quantity);
            ResultSet orderResult = getOrderStmt.executeQuery();

            if (orderResult.next()) {
                double totalSum = orderResult.getDouble("total_sum");

                // Update product quantity
                updateProductStmt.setInt(1, quantity);
                updateProductStmt.setString(2, productName);
                updateProductStmt.executeUpdate();

                // Update user balance
                updateUserBalanceStmt.setDouble(1, totalSum);
                updateUserBalanceStmt.setInt(2, userId);
                updateUserBalanceStmt.executeUpdate();

                // Delete order
                deleteOrderStmt.setInt(1, userId);
                deleteOrderStmt.setString(2, productName);
                deleteOrderStmt.setInt(3, quantity);
                deleteOrderStmt.executeUpdate();

                System.out.println("Return successful!");
                connection.commit();
            } else {
                System.out.println("Order not found for return!");
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }


}
class OrderDatabase{
    private Connection connection;

    public OrderDatabase() {
        try {
            connection = Database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Create Operation for Order
    public void addOrder(int userId, String productName, int quantity, double totalSum) throws SQLException {
        String insertOrderSQL = "INSERT INTO OrdersTable (user_id, product_name, quantity, total_sum) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertOrderStmt = connection.prepareStatement(insertOrderSQL)) {
            insertOrderStmt.setInt(1, userId);
            insertOrderStmt.setString(2, productName);
            insertOrderStmt.setInt(3, quantity);
            insertOrderStmt.setDouble(4, totalSum);
            insertOrderStmt.executeUpdate();

            System.out.println("Order added successfully!");
        }
    }
    //READ OPERATION
    public void showUserOrders(int userId) {
        String sql = "SELECT * FROM OrdersTable WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Orders for User " + userId + ":");
                while (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    double totalSum = resultSet.getDouble("total_sum");
                    System.out.println("Product: " + productName + " - Quantity: " + quantity + " - Total: $" + totalSum);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Delete Operation for Order
    public void deleteOrder(int UserorderId) {
        String sql = "DELETE FROM OrdersTable WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, UserorderId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Order deleted successfully!");
            } else {
                System.out.println("Order not found or delete failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Update Operation for Order (Update Quantity and Total Sum)
    public void updateOrder(int orderId, int newQuantity, double newTotalSum) {
        String sql = "UPDATE OrdersTable SET quantity = ?, total_sum = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newQuantity);
            statement.setDouble(2, newTotalSum);
            statement.setInt(3, orderId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order updated successfully!");
            } else {
                System.out.println("Order not found or update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



