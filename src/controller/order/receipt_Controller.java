package controller.order;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;

public class receipt_Controller {
    
    @FXML
    Text dateText, timeText;

    @FXML
    ImageView statusTrackerImage;

    @FXML
    Button cancelOrderButton;

    @FXML
    VBox orderCard;

    @FXML
    private Text orderNumText, custNameText, numOniText, totalAmountText, amountPaidText, paymentMText, changeText, custNoteText;

    static int orderNum;



    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    static ArrayList<ordered_items> orders = new ArrayList<ordered_items>();
    



    public void initialize() throws SQLException {
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

    public void updateDetailsOrder() throws SQLException {
        // Fetch and display order details using the orderNum
        fetchDataOrder();

        // count qty 
        int qty = 0;
        for (ordered_items order : orders) {
            qty += order.getQuantity();
        }
        numOniText.setText(String.valueOf(qty));

        updateContainerOrders();

        

    }

    public void updateContainerOrders() throws SQLException{
        orderCard.getChildren().clear();
        try {
            for (ordered_items orderItem : orders) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/widgets/add_order_pane.fxml"));
                Pane containerContent = loader.load(); // Load the container FXML
                orderCardController oCardController = loader.getController(); // Assign controller to variable
                oCardController.setData(orderItem);
                containerContent.setUserData(orderItem);
                containerContent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    try {
                        int clickedIndex = orders.indexOf(orderItem);
                        // Handle click event here
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                orderCard.getChildren().add(containerContent); // Add the container to the VBox
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    public void fetchDataOrder() {
        // Fetch ordered items from the database
        int sum = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM ordered_items WHERE order_NumOrder = ?"
            );
            preparedStatement.setInt(1, orderNum);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int orderNum = resultSet.getInt("order_NumOrder");
                int productID = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("qty");
                String isSpicy = resultSet.getString("isSpicy");
                int total = resultSet.getInt("total_amt");
                sum += total;
                ordered_items order = new ordered_items(orderNum, productID, isSpicy, quantity);
                orders.add(order);
            }
            totalAmountText.setText(String.valueOf(sum) + ".00 PHP");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo() {
        // Fetch user info from the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT order_CusName, order_NumOrder, order_Date, order_Time, order_Status, order_custNote, order_MOP, order_Change, order_AmntPaid " +
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
                String paymentMethod = resultSet.getString("order_MOP");
                double change = resultSet.getDouble("order_Change");
                double amountPaid = resultSet.getDouble("order_AmntPaid");
                // Set the user info to the text fields
                custNameText.setText(buyerName);
                dateText.setText(orderDate);
                timeText.setText(orderTime);
                custNoteText.setText(custNote);
                paymentMText.setText(paymentMethod);
                changeText.setText(String.valueOf(change) + "0 PHP");
                amountPaidText.setText(String.valueOf(amountPaid) + "0 PHP");
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
