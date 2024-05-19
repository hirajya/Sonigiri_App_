package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class order {
    private int order_NumOrder;
    private String order_custName;
    private String order_MOP;
    private double total_amt;
    private double amnt_paid;
    private String order_status;
    private String cust_note;
    private boolean discount10;

    public order(int order_NumOrder, String order_custName, String order_MOP, double total_amt, double amnt_paid, String order_status, String cust_note, boolean discount10) {
        this.order_NumOrder = order_NumOrder;
        this.order_custName = order_custName;
        this.order_MOP = order_MOP;
        this.total_amt = total_amt;
        this.amnt_paid = amnt_paid;
        this.order_status = "Pending";
        this.cust_note = cust_note;
        this.discount10 = discount10;
    }

    public boolean isDiscount10() {
        return discount10;
    }

    public void setDiscount10(boolean discount10) {
        this.discount10 = discount10;
    }

    public int getOrder_NumOrder() {
        return order_NumOrder;
    }

    public void setOrder_NumOrder(int order_NumOrder) {
        this.order_NumOrder = order_NumOrder;
    }

    public String getOrder_custName() {
        return order_custName;
    }

    public void setOrder_custName(String order_custName) {
        this.order_custName = order_custName;
    }

    public double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(double total_amt) {
        this.total_amt = total_amt;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public double getAmnt_paid() {
        return amnt_paid;
    }

    public void setAmnt_paid(double amnt_paid) {
        this.amnt_paid = amnt_paid;
    }

    public String getOrder_MOP() {
        return order_MOP;
    }

    public void setOrder_MOP(String order_MOP) {
        this.order_MOP = order_MOP;
    }

    public String getCust_note() {
        return cust_note;
    }

    public void setCust_note(String cust_note) {
        this.cust_note = cust_note;
    }

    public static void addOrder(order order) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Replace with your XAMPP connection details
            String url = "jdbc:mysql://localhost:3306/sonigiri_database";
            String username = "root";
            String password = "";

            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.err.println("Connected to database.");

            // Create a prepared statement to insert data
            String sql = "INSERT INTO order_table (order_Date, order_Time, order_Status, order_CusName, order_MOP, order_Change, order_AmntPaid, order_Total, order_custNote, discount10Percent, order_NumOrder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            

            LocalDate localDate = getCurrentDate();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);            
            String currentTime = getCurrentTime();
            double change;
            if (order.getOrder_MOP().equals("GCash")) {
                change = 0.0;
            } else {
                change = order.getAmnt_paid() - order.getTotal_amt();

            }

            // Set values for the prepared statement
            preparedStatement.setDate(1, sqlDate); 
            preparedStatement.setString(2, currentTime);
            preparedStatement.setString(3, order.getOrder_status());
            preparedStatement.setString(4, order.getOrder_custName());
            preparedStatement.setString(5, order.getOrder_MOP());
            preparedStatement.setDouble(6, change);
            preparedStatement.setDouble(7, order.getAmnt_paid());
            preparedStatement.setDouble(8, order.getTotal_amt());
            preparedStatement.setString(9, order.getCust_note());
            preparedStatement.setBoolean(10, order.isDiscount10());
            preparedStatement.setInt(11, order.getOrder_NumOrder());

            // Execute the update and check for success
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Order added successfully!");
            } else {
                System.out.println("Error adding order: Unexpected number of rows affected");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database or adding order: " + e.getMessage());
        } finally {
            // Close resources (connection and prepared statement)
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
    }

    public static String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = currentTime.format(timeFormatter);
        return formattedTime;        
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static String getOrderNumCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Replace with your XAMPP connection details
            String url = "jdbc:mysql://localhost:3306/sonigiri_database";
            String username = "root";
            String password = "";
    
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.err.println("Connected to database.");
    
            // Create a prepared statement to insert data
            String sql = "SELECT COUNT(order_NumOrder) FROM order_table";
            preparedStatement = connection.prepareStatement(sql);
    
            // Execute the query and get the result set
            resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int orderNumCount = resultSet.getInt("COUNT(order_NumOrder)");
                System.out.println("Order count: " + orderNumCount);
                // Return the count as a String directly within the if block
                return String.valueOf(orderNumCount);
            } else {
                System.out.println("No orders found");
                // If no orders found, return "0" or an appropriate default value
                return "0";
            }
    
        } catch (SQLException e) {
            System.out.println("Error connecting to database or adding order: " + e.getMessage());
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
        return null;
    
        // This line is unreachable because a return statement is already inside the try block
        // return String.valueOf(resultSet.getInt("COUNT(order_NumOrder)")); 
    }
    

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        System.out.println(getOrderNumCount());    
        
    }
      
    
    

}
