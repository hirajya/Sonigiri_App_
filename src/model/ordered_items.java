package model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

public class ordered_items {
    private int order_NumOrder;
    private int product_id;
    private String isSpicy;
    private int quantity;
    private double sub_total;

    public ordered_items(int order_NumOrder, int product_id, String isSpicy, int quantity) {
        this.order_NumOrder = order_NumOrder;
        this.product_id = product_id;
        this.isSpicy = isSpicy;
        this.quantity = quantity;
    }

    public int getOrder_NumOrder() {
        return order_NumOrder;
    }

    public void setOrder_NumOrder(int order_NumOrder) {
        this.order_NumOrder = order_NumOrder;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String isSpicy() {
        return isSpicy;
    }

    public void setSpicy(String isSpicy) {
        this.isSpicy = isSpicy;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return sub_total;
    }

    public void setPrice(double price) {
        this.sub_total = price;
    }

    public static void addOrder(ordered_items orderItem) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // Replace with your XAMPP connection details
            String url = "jdbc:mysql://localhost:3306/sonigiri_database";
            String username = "root";
            String password = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            double sub_total1 = orderItem.quantity * findProductPrice(connection, orderItem.getProduct_id());
            orderItem.setPrice(sub_total1);

            // Connect to the database
            connection = DriverManager.getConnection(url, username, password);
            String sql = "INSERT INTO ordered_items (order_NumOrder, product_id, isSpicy, qty, total_amt) VALUES (?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderItem.getOrder_NumOrder());
            ps.setInt(2, orderItem.getProduct_id());
            ps.setString(3, orderItem.isSpicy());
            ps.setInt(4, orderItem.getQuantity());
            ps.setDouble(5, orderItem.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static double findProductPrice(Connection connection, int productID) throws SQLException {
        double productPrice = 0.0; // Initialize to default value
    
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    
        try {
            // ... (connection establishment code - assuming you have a way to establish a connection)
    
            String sql = "SELECT product_price FROM product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
    
            // Set the value for the product_id parameter
            preparedStatement.setInt(1, productID);
    
            // Execute the query and get the result set
            resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                productPrice = resultSet.getDouble("product_price");
            } else {
                System.out.println("Product not found with ID: " + productID);
            }
        } finally {
            // Close resources (prepared statement and result set)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error closing result set: " + e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println("Error closing prepared statement: " + e.getMessage());
                }
            }
        }
    
        return productPrice;
    }

    public static double findProductPriceSimple(int productID) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double productPrice = 0.0; // Initialize to default value
    
        try {
            // Replace with your XAMPP connection details
            String url = "jdbc:mysql://localhost:3306/sonigiri_database";
            String username = "root";
            String password = "";
    
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.err.println("Connected to database.");
    
            // Create a prepared statement to select product price
            String sql = "SELECT product_price FROM product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
    
            // Set the value for the product_id parameter
            preparedStatement.setInt(1, productID);
    
            // Execute the query and get the result set
            resultSet = preparedStatement.executeQuery();
    
            // Check if a product is found
            if (resultSet.next()) {
                productPrice = resultSet.getDouble("product_price");
            } else {
                System.out.println("Product not found with ID: " + productID);
            }
    
        } catch (SQLException e) {
            System.out.println("Error connecting to database or retrieving product price: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading MySQL driver: " + e.getMessage());
        } finally {
            // Close resources (connection, prepared statement, and result set)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error closing result set: " + e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println("Error closing prepared statement: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    
        return productPrice;
    }
    

    public static String getProduct_name(int productId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String productName = null;
    
        try {
            // Replace with your XAMPP connection details
            String url = "jdbc:mysql://localhost:3306/sonigiri_database";
            String username = "root";
            String password = "";
    
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.err.println("Connected to database.");
    
            // Create a prepared statement to select product name
            String sql = "SELECT product_name FROM product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
    
            // Set the value for the placeholder in the query
            preparedStatement.setInt(1, productId);
    
            // Execute the query and get the result set
            resultSet = preparedStatement.executeQuery();
    
            // Check if a product is found
            if (resultSet.next()) {
                productName = resultSet.getString("product_name");
            } else {
                System.out.println("Product with ID " + productId + " not found.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error connecting to database or retrieving product name: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading MySQL driver: " + e.getMessage());
        } finally {
            // Close resources (connection, prepared statement, and result set)
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error closing result set: " + e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println("Error closing prepared statement: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    
        return productName;
    }

    public static void printOrderedItems(ArrayList<ordered_items> items) {
        System.out.println("==========START OF ORDERED ITEMS==========");
        for (ordered_items item : items) {
            System.out.println("Order Number: " + item.getOrder_NumOrder());
            System.out.println("Product ID: " + item.getProduct_id());
            System.out.println("Is Spicy: " + item.isSpicy());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Price: " + item.getPrice());
            System.out.println("---------------------------------------");
        }
        System.out.println("==========END OF ORDERED ITEMS==========");
    }

    public static int getSoldCountForProduct(String productName) throws SQLException {
        int soldCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sonigiri_database", "root", "");

            // SQL query to get the sold count for a specific product in completed orders
            String query = "SELECT SUM(ordered_items.qty) AS sold_count FROM ordered_items " +
                           "INNER JOIN order_table ON ordered_items.order_NumOrder = order_table.order_NumOrder " +
                           "INNER JOIN product ON ordered_items.product_id = product.product_id " +
                           "WHERE product.product_name = ? AND order_table.order_Status = 'Done'";

            // Create the prepared statement
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productName);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the result set
            if (resultSet.next()) {
                soldCount = resultSet.getInt("sold_count");
            }
        } finally {
            // Close resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return soldCount;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ordered_items orderItem = new ordered_items(1, 1, "false", 2);
        addOrder(orderItem);
        
    }
    
}
