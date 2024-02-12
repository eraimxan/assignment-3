
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection;

        try {
            connection = Database.connect();
            UserDatabase user = new UserDatabase();
            OrderDatabase order = new OrderDatabase();
            ProductDatabase product = new ProductDatabase();

            Scanner scanner = new Scanner(System.in);

            int choice;
            do {
                System.out.println("\nHello! You have the following available functions:");
                System.out.println("1) To show products list;");
                System.out.println("2) To add a product;");
                System.out.println("3) To add a new user;");
                System.out.println("4) To buy product;");
                System.out.println("5) To return a product;");
                System.out.println("6) To show all users;");
                System.out.println("7) To show the certain user’s orders.");
                System.out.println("8) To delete the certain user;");
                System.out.println("9) To delete the certain product;");
                System.out.println("10) To delete the order;");
                System.out.println("11) To update the quantity of the product;");
                System.out.println("12) To update the balance of the user;");
                System.out.println("13) To update the order;");
                System.out.println("0) Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        product.showProductList();
                        break;
                    case 2:
                        System.out.print("Enter product name: ");
                        scanner.nextLine(); // Consume the newline character
                        String productName = scanner.nextLine();
                        System.out.print("Enter product cost: ");
                        double productCost = scanner.nextDouble();
                        System.out.print("Enter product quantity: ");
                        int productQuantity = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.print("Enter product description: ");
                        String productDescription = scanner.nextLine();
                        product.addProduct(productName, productCost, productQuantity, productDescription);
                        break;
                    case 3:
                        System.out.print("Enter user ID: ");
                        int userId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.print("Enter user name: ");
                        String userName = scanner.nextLine();
                        System.out.print("Enter user balance: ");
                        double userBalance = scanner.nextDouble();
                        user.addUser(userId, userName, userBalance);
                        break;
                    case 4:
                        System.out.print("Enter user ID: ");
                        int buyUserId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.print("Enter product name to buy: ");
                        String buyProductName = scanner.nextLine();
                        System.out.print("Enter quantity to buy: ");
                        int buyQuantity = scanner.nextInt();
                        product.buyProduct(buyUserId, buyProductName, buyQuantity);
                        break;
                    case 5:
                        System.out.print("Enter user ID: ");
                        int returnUserId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.print("Enter product name to return: ");
                        String returnProductName = scanner.nextLine();
                        System.out.print("Enter quantity to return: ");
                        int returnQuantity = scanner.nextInt();
                        product.returnProduct(returnUserId, returnProductName, returnQuantity);
                        break;
                    case 6:
                        user.showAllUsers();
                        break;
                    case 7:
                        System.out.print("Enter user ID to show orders: ");
                        int showOrdersUserId = scanner.nextInt();
                        order.showUserOrders(showOrdersUserId);
                        break;
                    case 8:
                        scanner.nextLine();
                        System.out.print("Enter user ID to delete the user: ");
                        int userID = scanner.nextInt();
                        user.deleteUser(userID);
                        break;
                    case 9:
                        System.out.print("Enter product name to delete it: ");
                        scanner.nextLine();
                        String productname  = scanner.nextLine();
                        product.deleteProduct(productname);
                        break;
                    case 10:
                        System.out.print("Enter the user ID to delete the order: ");
                        scanner.nextLine();
                        int user_id = scanner.nextInt();
                        order.deleteOrder(user_id);
                    case 11:
                        System.out.print("Enter the name of the product: ");
                        scanner.nextLine();
                        productname = scanner.nextLine();
                        System.out.print("Enter the quantity that you want to change: " );
                        int quantity = scanner.nextInt();
                        product.updateProductQuantity(productname, quantity);
                    case 12:
                        System.out.print("Enter the user ID: ");
                        scanner.nextLine();
                        user_id = scanner.nextInt();
                        System.out.print("Enter the changes in your balance: " );
                        int newbalance = scanner.nextInt();
                        user.updateUserBalance(user_id, newbalance);
                    case 13:
                        System.out.print("Enter the user ID: ");
                        scanner.nextLine();
                        user_id = scanner.nextInt();
                        System.out.print("Enter the changes in quantity: " );
                        quantity = scanner.nextInt();
                        System.out.println("Enter the changes in total sum: ");
                        int totalsum = scanner.nextInt();
                        order.updateOrder(user_id, quantity, totalsum);
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
