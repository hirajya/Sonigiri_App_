package controller.order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.mainController;
import controller.order.table_ordersController.OrderView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.ordered_items;

public class receipt_Controller {
    
    @FXML
    Text dateText, timeText;

    @FXML
    ImageView statusTrackerImage;

    @FXML
    Button cancelOrderButton;

    @FXML
    private Text orderNumText, custNameText, numOniText, totalAmountText, amountPaidText, paymentMText, changeText, custNoteText;

    static int orderNum;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    static ArrayList<ordered_items> orders = new ArrayList<ordered_items>();
    



    public void initialize() {
        // Set the order number
        orderNumText.setText(String.valueOf(orderNum));
        System.out.println("Order Number: " + orderNum);
        getUserInfo();
        updateDetailsOrder();
        // Fetch and display order details using the orderNum
    }

    public void handleCancelOrderButton() {
        
    }

    public void handleBackButton() {
        // Go back to the previous page
    }

    public void updateDetailsOrder() {
        // Fetch and display order details using the orderNum

    }

    // private void fetchData() {
    //     ObservableList<OrderView> orders = FXCollections.observableArrayList();
    
    //     try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
    //         PreparedStatement preparedStatement = connection.prepareStatement(
    //             "SELECT o.order_NumOrder, o.order_CusName AS buyerName, o.order_Date, o.order_Time, o.order_Status, " +
    //             "i.product_id, i.isSpicy, i.qty " +
    //             "FROM order_table o " +
    //             "LEFT JOIN ordered_items i ON o.order_NumOrder = i.order_NumOrder " +
    //             "LEFT JOIN product p ON i.product_id = p.product_id"
    //         );
    
    //         ResultSet resultSet = preparedStatement.executeQuery();
    //         Map<Integer, OrderView> orderMap = new HashMap<>();
    //         while (resultSet.next()) {
    //             int orderNum = resultSet.getInt("order_NumOrder");
    //             String buyerName = resultSet.getString("buyerName");
    //             String orderDate = resultSet.getString("order_Date");
    //             String orderTime = resultSet.getString("order_Time");
    //             String PStatus = resultSet.getString("order_Status");
    //             int productId = resultSet.getInt("product_id");
    //             String isSpicy = resultSet.getString("isSpicy");
    //             int qty = resultSet.getInt("qty");
    
    //             OrderView orderView;
    //             if (!orderMap.containsKey(orderNum)) {
    //                 orderView = new OrderView(orderNum, buyerName, orderDate, orderTime, 0, 0, 0, 0, 0, 0, PStatus);
    //                 orderMap.put(orderNum, orderView);
    //                 orders.add(orderView);
    //             } else {
    //                 orderView = orderMap.get(orderNum);
    //             }
    
    //             // Adjust quantity based on spiciness
    //             if (isSpicy.equals("Spicy")) {
    //                 switch (productId) {
    //                     case 1: // Product ID for Tuna Mayo
    //                         orderView.setSpicyTunaMayoCount(qty);
    //                         break;
    //                     case 2: // Product ID for Bulgogi
    //                         orderView.setSpicyBulgogiCount(qty);
    //                         break;
    //                     case 3: // Product ID for Chicken Adobo
    //                         orderView.setSpicyChickenAdoboCount(qty);
    //                         break;
    //                     // Add more cases if needed for other products
    //                 }
    //             } else if (isSpicy.equals("Not Spicy")) {
    //                 switch (productId) {
    //                     case 1: // Product ID for Tuna Mayo
    //                         orderView.setNotSpicyTunaMayoCount(qty);
    //                         break;
    //                     case 2: // Product ID for Bulgogi
    //                         orderView.setNotSpicyBulgogiCount(qty);
    //                         break;
    //                     case 3: // Product ID for Chicken Adobo
    //                         orderView.setNotSpicyChickenAdoboCount(qty);
    //                         break;
    //                     // Add more cases if needed for other products
    //                 }
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    
    //     tableViewOrder.setItems(orders);
    // }

    public void getUserInfo() {
        // Fetch user info from the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT order_CusName, order_NumOrder, order_Date, order_Time, order_Status, order_custNote " +
                "FROM order_table " +
                "WHERE order_NumOrder = ?"
            );
            preparedStatement.setInt(1, orderNum);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String buyerName = resultSet.getString("order_CusName");
                String orderDate = resultSet.getString("order_Date");
                String orderTime = resultSet.getString("order_Time");
                String orderStatus = resultSet.getString("order_Status");
                String custNote = resultSet.getString("order_custNote");
                custNameText.setText(buyerName);
                dateText.setText(orderDate);
                timeText.setText(orderTime);
                custNoteText.setText(custNote);
                // Set the status tracker image based on the order status
                // if (orderStatus.equals("Pending")) {
                //     statusTrackerImage.setImage(null);
                // } else if (orderStatus.equals("Confirmed")) {
                //     statusTrackerImage.setImage(null);
                // } else if (orderStatus.equals("Delivered")) {
                //     statusTrackerImage.setImage(null);
                // }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
