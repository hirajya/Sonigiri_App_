package controller.order;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;

public class customer_infoController {
    
    @FXML
    Button go_paymentInfo_btn;

    @FXML
    Text numOniText, orderNumText, totalAmountText;

    @FXML
    VBox orderCard;

    @FXML
    Pane custInfoPane, orderSumPane;

    @FXML
    TextField custNameField;

    @FXML
    TextArea custNoteField;

    @FXML
    Button backButton1;

    static ArrayList<ordered_items> ordersListl;
    static int orderNumCurrent;
    static String custName;
    static String custNote;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";



    @FXML
    public void initialize() throws SQLException {
        ordered_items.printOrderedItems(ordersListl);
        orderNumCurrent = getNextAvailableOrderNumber();
        noSideRectangles();
        refreshTableOrder();
        setNumOniText();
        setTotalAmountText();
        setOrderNumText();
    }

    private int getNextAvailableOrderNumber() {
        int nextOrderNumber = Integer.parseInt(order.getOrderNumCount()) + 1;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT order_NumOrder FROM order_table ORDER BY order_NumOrder";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            int expectedOrderNumber = 1;

            while (resultSet.next()) {
                int currentOrderNumber = resultSet.getInt("order_NumOrder");

                if (currentOrderNumber != expectedOrderNumber) {
                    nextOrderNumber = expectedOrderNumber;
                    break;
                }
                expectedOrderNumber++;
            }

            if (nextOrderNumber == expectedOrderNumber) {
                nextOrderNumber = expectedOrderNumber;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextOrderNumber;
    }

    public void refreshTableOrder() throws SQLException {
        orderCard.getChildren().clear();
        try {
            for (ordered_items orderItem : ordersListl) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/widgets/add_order_pane.fxml"));
                Pane containerContent = loader.load(); // Load the container FXML
                orderCardController oCardController = loader.getController(); // Assign controller to variable
                oCardController.setData(orderItem);
                containerContent.setUserData(orderItem);
                containerContent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    try {
                        int clickedIndex = ordersListl.indexOf(orderItem);
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
    

    

    public void handleGoPaymentInfoButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) go_paymentInfo_btn.getScene().getRoot().getUserData();
            payment_infoController.ordersList2 = ordersListl;
            String custName = custNameField.getText();
            String custNote = custNoteField.getText();
            
            customer_infoController.custName = custName;
            customer_infoController.custNote = custNote;
            payment_infoController.custName = custName;
            payment_infoController.custNote = custNote;


            
            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/payment_info.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToOrder() throws SQLException {
        try {
            // Get reference to the main controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/order.fxml"));
            Pane root = loader.load();
            mainController mainController = (mainController) backButton1.getScene().getRoot().getUserData();
            orderController oc = loader.getController();
            oc.refreshTable();
            
            // Pass the main controller reference to the order controller
            oc.setMainController(mainController);
    
            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/order.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void noSideRectangles() {
        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(custInfoPane.getPrefWidth());
        clipRect.setHeight(custInfoPane.getPrefHeight());
        clipRect.setArcWidth(20); // Adjust for desired corner radius
        clipRect.setArcHeight(20); // Adjust for desired corner radius

        custInfoPane.setClip(clipRect); // Set the clip to the pane

        // Set background color for the Pane (optional)
        custInfoPane.setStyle("-fx-background-color: FFFFFF;");


        Rectangle clipRect2 = new Rectangle();
        clipRect2.setWidth(orderSumPane.getPrefWidth());
        clipRect2.setHeight(orderSumPane.getPrefHeight());
        clipRect2.setArcWidth(20); // Adjust for desired corner radius
        clipRect2.setArcHeight(20); // Adjust for desired corner radius

        orderSumPane.setClip(clipRect2); // Set the clip to the pane

        // Set background color for the Pane (optional)
        orderSumPane.setStyle("-fx-background-color: FFFFFF;");

    }

    public void setOrderNumText() {
        orderNumText.setText("Order #" + orderNumCurrent);
    }

    public void setNumOniText() {
        int numberOfOnigiri = 0;
        for (ordered_items order : ordersListl) {
            numberOfOnigiri += order.getQuantity();
        }
        numOniText.setText(String.valueOf(numberOfOnigiri));
    }

    public void setTotalAmountText() throws SQLException {
        double totalAmount = 0.0;
        int onigiriCount = 0;
        int discountCount = 0; // Count for every 4th onigiri
    
        // Calculate total amount with discount
        for (ordered_items order : ordersListl) {
            double price = ordered_items.findProductPriceSimple(order.getProduct_id());
            int quantity = order.getQuantity();
    
            for (int i = 1; i <= quantity; i++) {
                onigiriCount++;
                if (onigiriCount % 4 == 0) {
                    // Apply 50% discount for every 4th onigiri
                    totalAmount += price * 0.5;
                    discountCount++; // Increment discount count
                } else {
                    totalAmount += price;
                }
            }
        }
    
        // If there are any discounted onigiri, subtract the total discount amount
        totalAmount -= (discountCount / 4) * (ordered_items.findProductPriceSimple(1) * 0.5);
    
        // Round up the total amount to the nearest integer value
        totalAmount = Math.ceil(totalAmount);
    
        totalAmountText.setText(String.format("%.0f Php", totalAmount)); // Display as integer value
    }

    public static void main(String[] args) {
        // System.out.println(go_paymentInfo_btn);
    }
    
}
